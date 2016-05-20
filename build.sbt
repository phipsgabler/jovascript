name := "jovascript"

version := "0.1.0"

scalaVersion := "2.11.2"

mainClass in (Compile, run) := Some("jovascript.JovascriptCompiler")

antlr4Settings

antlr4GenListener in Antlr4 := true

antlr4GenVisitor in Antlr4 := true

antlr4Dependency in Antlr4 := "org.antlr" % "antlr4" % "4.5"

antlr4PackageName in Antlr4 := Some("jovascript.parser")
