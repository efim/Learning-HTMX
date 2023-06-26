{ pkgs, sbt-derivation }:

let
  package = sbt-derivation.lib.mkSbtDerivation {
      inherit pkgs;
      # ...and the rest of the arguments
      pname = "price-grid-app";
      version = "0.0.1";
      src = pkgs.nix-gitignore.gitignoreSource [] ./.;
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
        cp target/scala-*/priceGrid-assembly-*.jar $out/bin/
        mkdir -p $out/bin/dist
        cp ./output.css $out/bin/dist/output.css
        cp -r public $out/bin/public
      '';

      depsSha256 = "sha256-aWLqnPXvchtNqpSfXo5sWyK2QFNn1GqToy58cWrML3U=";
    };

  module = { config, pkgs, ... }: {
    services.price-grid-app = {
      enable = true;
      package = package;
    };
  };
in
{
  package = package;
  module = module;
}
