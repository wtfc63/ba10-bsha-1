#!/bin/sh

if [ $# -gt 0 ]
then
  saxon-xslt -o test.svg test.xml graphml2svg.xslt offset=$1
  saxon-xslt -o out.svg graph.xml graphml2svg.xslt offset=$1
  echo "> SVGs 'test.svg' and 'out.svg' created."
else
  saxon-xslt -o test.svg test.xml graphml2svg.xslt
  saxon-xslt -o out.svg graph.xml graphml2svg.xslt
  echo "> SVGs 'test.svg' and 'out.svg' created. (Tip: use the first argument to this script to set the vertical offset of graph)"
fi