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
          lib = pkgs.lib;
        };
        order-summary = import ./12-order-summary-component-thymeleaf/default.nix {
          inherit pkgs sbt-derivation;
          lib = pkgs.lib;
        };
        testimonials-grid = import ./13-testimonials-grid-section/default.nix {
          inherit pkgs sbt-derivation;
          lib = pkgs.lib;
        };
        rock-paper-scissors = import ./14-rock-paper-scissors/default.nix {
          inherit pkgs sbt-derivation;
          lib = pkgs.lib;
        };
      in {
        devShells.default = pkgs.mkShell {
          buildInputs = [
            pkgs.scala-cli
            pkgs.sbt
            pkgs.scalafmt
            pkgs.nodePackages.tailwindcss
            pkgs.nodePackages.prettier
            pkgs.jdk
          ];
        };
        packages.price-grid-app = price-grid.package;
        nixosModules.price-grid-app = price-grid.module;
        packages.order-summary = order-summary.package;
        nixosModules.order-summary = order-summary.module;
        packages.testimonials-grid = testimonials-grid.package;
        nixosModules.testimonials-grid = testimonials-grid.module;
        packages.rock-paper-scissors = rock-paper-scissors.package;
        packages.rock-paper-scissors-image = rock-paper-scissors.image;
        nixosModules.rock-paper-scissors = rock-paper-scissors.module;
      });
  # see https://serokell.io/blog/practical-nix-flakes
}
