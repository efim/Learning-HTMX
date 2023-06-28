{ pkgs, lib, sbt-derivation }:

let
  package = sbt-derivation.lib.mkSbtDerivation {
    inherit pkgs;
    # ...and the rest of the arguments
    pname = "order-summary-component-app";
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
      cp target/scala-*/order-summary-component-assembly-*.jar $out/bin/order-summary-component.jar
      mkdir -p $out/bin/dist
      cp ./output.css $out/bin/dist/output.css
      cp -r public $out/bin/public
    '';

    depsSha256 = "sha256-ADQB4qTl69ERlLAURrtR3fWa7PUdYjFLk5QdU5QgxRQ=";
  };

  module = { config, pkgs, ... }:
    let cfg = config.services.orderSummaryComponent;
    in {
      options.services.orderSummaryComponent = {
        enable = lib.mkEnableOption "My service";

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
        users.groups.order-summary-component-group = { };
        users.users.order-summary-component-user = {
          isSystemUser = true;
          group = "order-summary-component-group";
        };

        systemd.services.orderSummaryComponent =
          let serverHost = if cfg.useNginx then "localhost" else cfg.host;
          in {
            description = "Exercise app Order Summary Component";
            wantedBy = [ "multi-user.target" ];
            after = [ "network.target" ];
            startLimitIntervalSec = 30;
            startLimitBurst = 10;
            serviceConfig = {
              ExecStart =
                "${pkgs.jdk}/bin/java -jar ${package}/bin/order-summary-component.jar -p ${
                  toString cfg.port
                } --host ${serverHost}";
              WorkingDirectory = "${package}/bin";
              Restart = "on-failure";
              User = "order-summary-component-user";
              Group = "order-summary-component-group";
            };
          };

        services.nginx = lib.mkIf cfg.useNginx {
          virtualHosts.${cfg.host} = {
            locations."/".proxyPass = "http://127.0.0.1:${toString cfg.port}";
          };
        };
      };
    };
in {
  package = package;
  module = module;
}
