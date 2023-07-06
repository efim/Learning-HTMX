{ pkgs, lib, sbt-derivation }:

let
  pname = "testimonials-grid-section";
  package = sbt-derivation.lib.mkSbtDerivation {
    inherit pkgs pname;
    # ...and the rest of the arguments
    version = "0.0.1";
    src = pkgs.nix-gitignore.gitignoreSource [ ] ./.;
    nativeBuildInputs = [ pkgs.nodePackages.tailwindcss ];
    buildPhase = ''
      sbt assembly
      tailwindcss -i ./src/input.css -o ./output.css
    '';
    # css path different from ordinary development,
    # because .gitignore makes it unavailable during nix build
    # anyway copied to correct place
    installPhase = ''
      mkdir -p $out/bin
      cp target/scala-*/${pname}-assembly-*.jar $out/bin/${pname}.jar
      mkdir -p $out/bin/dist
      cp ./output.css $out/bin/dist/output.css
      cp -r public $out/bin/public
    '';

    depsSha256 = "sha256-Y5RktcE3fxUJci4o7LTuNlBEybTdVRqsG551AkVeRPw=";
  };

in {
  inherit package;
  module = { config, pkgs, ... }:
    let cfg = config.services.${pname};
    in {
      options.services.${pname} = {
        enable = lib.mkEnableOption "My frontendmentor exercise ${pname}";

        port = lib.mkOption {
          type = lib.types.int;
          default = 8080;
          description = "Port to listen on.";
        };

        host = lib.mkOption {
          type = lib.types.str;
          default = "localhost";
          description = "Host to bind to.";
        };

        useNginx = lib.mkOption {
          type = lib.types.bool;
          default = true;
          description = "Whether to use Nginx to proxy requests.";
        };
      };
      config = lib.mkIf cfg.enable {
        users.groups."${pname}-group" = { };
        users.users."${pname}-user" = {
          isSystemUser = true;
          group = "${pname}-group";
        };

        systemd.services.${pname} =
          let serverHost = if cfg.useNginx then "localhost" else cfg.host;
          in {
            description = "Exercise app ${pname}";
            wantedBy = [ "multi-user.target" ];
            after = [ "network.target" ];
            startLimitIntervalSec = 30;
            startLimitBurst = 10;
            serviceConfig = {
              ExecStart =
                "${pkgs.jdk}/bin/java -jar ${package}/bin/${pname}.jar -p ${
                  toString cfg.port
                } --host ${serverHost}";
              WorkingDirectory = "${package}/bin";
              Restart = "on-failure";
              User = "${pname}-user";
              Group = "${pname}-group";
            };
          };

        services.nginx = lib.mkIf cfg.useNginx {
          virtualHosts.${cfg.host} = {
            locations."/".proxyPass = "http://127.0.0.1:${toString cfg.port}";
          };
        };
      };
    };
  image = pkgs.dockerTools.buildLayeredImage {
    name = pname;
    tag = "latest";
    created = "now";
    config = {
      Cmd = [
        "${pkgs.jdk}/bin/java"
        "-jar"
        "${package}/bin/${pname}.jar"
        "--host"
        "0.0.0.0"
      ];
      ExposedPorts = { "8080/tcp" = { }; };
      WorkingDir = "${package}/bin";
    };
  };
}
