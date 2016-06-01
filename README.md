# Jovascript

A small functional language I'm experimenting with, compiled to Javascript. I plan to experiment with at type system based 
on record types, together with union and intersection types (similar to [Ceylon](http://ceylon-lang.org)). But everything 
is still open...


## Setup

```
> # get dependencies and compile
> sbt clean update compile

> # run to parse input to AST:
> sbt run
let x: Int = 1;
ProgramNode(ArrayBuffer(ValueDefinitionNode('x,TypeNameNode('Int),NumberLiteralNode(1))))
```
