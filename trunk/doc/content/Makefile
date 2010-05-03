document.pdf: document.toc document.bbl
	pdflatex document

document.toc: $(wildcard *.tex) $(wildcard content/*.tex)
	latex document

document.bbl: etc/bibliography.bib
	latex document
	bibtex document
	latex document
	latex document
