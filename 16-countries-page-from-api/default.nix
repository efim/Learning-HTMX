{ pkgs, lib, sbt-derivation }:
let pname = "countries-page";
in rec {
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

    depsSha256 = "sha256-vdCU7UJAGi/CujA05OVw/lIomMpIFl+kW+pn8ny16JI=";
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
    };
  };

}
