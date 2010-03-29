document.pdf: document.toc document.bbl
	pdflatex document

document.toc: $(wildcard *.tex) $(wildcard content/*.tex)
	latex document

document.bbl: etc/references.bib etc/bibliography.tex
	latex document
	bibtex document
	latex document
	latex document
