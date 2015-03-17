# Introduction #

The documentation of this project ist written in LaTEX.


# Environment installation #

For Ubuntu, the following packages are need in addition to _Texlive_
  * glossaries
  * xfor

_Texlive_ and the additional packages can be install with the following commands:
```
sudo apt-get install texlive-latex-extra
cd /usr/local/share/texmf/
sudo wget http://www.ctan.org/get/install/macros/latex/contrib/glossaries.tds.zip
sudo wget http://www.ctan.org/get/install/macros/latex/contrib/xfor.tds.zip
sudo unzip glossaries.tds.zip
sudo unzip xfor.tds.zip
sudo texhash
```

# SVG images #

To include SVG images, the easiest way seems to be to convert them to PDF (which both Google Docs and Inkscape can do) and to include the PDF.

However one could also automate that process with Inkscape, see: [SVG\_in\_LaTeX.pdf](http://wiki.inkscape.org/wiki/images/SVG_in_LaTeX.pdf).

But thats probably not something we need to do right away...