{ pkgs, lib, sbt-derivation }:

let
  pname = "multi-step-form";
  package = sbt-derivation.lib.mkSbtDerivation {
    inherit pkgs pname;
    # ...and the rest of the arguments
    version = "0.0.1";
    src = pkgs.nix-gitignore.gitignoreSource [ ] ./.;
    nativeBuildInputs = [ pkgs.nodePackages.tailwindcss ];
    buildPhase = ''
      tailwindcss -i ./src/input.css -o ./src/main/resources/public/output.css
      sbt assembly
    '';
    installPhase = ''
      mkdir -p $out/bin
      cp target/scala-*/${pname}-assembly-*.jar $out/bin/${pname}.jar
    '';

    depsSha256 = "sha256-zG4e7ERdi/WxzACymaYUQ0x8v4/peGARuqPAK8xvBmE=";
  };

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
      Cmd = [ "${pkgs.jdk}/bin/java" "-jar" "${package}/bin/${pname}.jar" "--host" "0.0.0.0" ];
      ExposedPorts = {
        "8080/tcp" = {};
      };
    };
  };
  # image =     pkgs.dockerTools.buildLayeredImage { # so, wow, this works
  #     name = "hello2";
  #     tag = "latest";
  #     config.Cmd = [ "${pkgs.hello}/bin/hello" ];
  #   };
in {
  package = package;
  module = module;
  image = image;
}
