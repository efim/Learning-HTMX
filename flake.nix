{
  description = "learning htmx";
  inputs.nixpkgs.url = "github:nixos/nixpkgs";
  inputs.flake-utils.url = "github:numtide/flake-utils";
  # add this line
  inputs.sbt-derivation.url = "github:zaninime/sbt-derivation";
  # recommended for first style of usage documented below, but not necessary
  inputs.sbt-derivation.inputs.nixpkgs.follows = "nixpkgs";

  outputs = { self, nixpkgs, flake-utils, sbt-derivation }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        price-grid = import ./11-single-price-grid-component/default.nix {
          inherit pkgs sbt-derivation;
        };
      in {
        devShells.default = pkgs.mkShell {
          buildInputs = [
            pkgs.scala-cli
            pkgs.sbt
            pkgs.scalafmt
            pkgs.nodePackages.tailwindcss
            pkgs.jdk
          ];
        };
        packages.price-grid-app = price-grid.package;
        nixosModules.price-grid-app = price-grid.module;
      });
  # see https://serokell.io/blog/practical-nix-flakes
}
