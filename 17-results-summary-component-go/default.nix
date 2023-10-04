{ lib, pkgs, ... }:
let
  pname = "results-summary-component";
  version = "0.0.1";
in rec {
  package = pkgs.buildGoModule {
    inherit pname version;
    src = pkgs.nix-gitignore.gitignoreSource [ ] ./.;
    vendorHash = null; # set to "" when get dependencies in go.mod
  };
  image = pkgs.dockerTools.buildLayeredImage {
    name = pname;
    tag = "latest";
    created = "now";
    config = {
      Cmd = [
        "${package}/bin/results-summary-component-go"
        "-p"
        "8080"
        "-h"
        "0.0.0.0"
      ];
      ExposedPorts = { "8080/tcp" = {  }; };
    };
  };
  # nixos module
}
