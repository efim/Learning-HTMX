{ pkgs, lib, sbt-derivation }:

let
  package = sbt-derivation.lib.mkSbtDerivation {
    inherit pkgs;
    # ...and the rest of the arguments
    pname = "price-grid-app";
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
      cp target/scala-*/priceGrid-assembly-*.jar $out/bin/priceGridApp.jar
      mkdir -p $out/bin/dist
      cp ./output.css $out/bin/dist/output.css
      cp -r public $out/bin/public
    '';

    depsSha256 = "sha256-aWLqnPXvchtNqpSfXo5sWyK2QFNn1GqToy58cWrML3U=";
  };

  module = { config, pkgs, ... }:
    let cfg = config.services.priceGridService;
    in {
      options.services.priceGridService = {
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
      };
      config = lib.mkIf cfg.enable {
        users.groups.price-grid-app-group = { };
        users.users.price-grid-app-user = {
          isSystemUser = true;
          group = "price-grid-app-group";
        };

        systemd.services.price-grid-app = {
          description = "My Java Service";
          wantedBy = [ "multi-user.target" ];
          after = [ "network.target" ];

          serviceConfig = {
            ExecStart =
              "${pkgs.jdk}/bin/java -jar ${package}/bin/priceGridApp.jar -p ${toString cfg.port} --host ${cfg.host}";
            Restart = "on-failure";
            User = "price-grid-app-user";
            Group = "price-grid-app-group";
          };
        };
      };
    };
in {
  package = package;
  module = module;
}
