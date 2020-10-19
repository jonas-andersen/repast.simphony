#!/bin/sh

convert -colors 256 repast-16.png tmp.gif
convert -colors 256 tmp.gif repast-16-8bpp.png
convert -colors 256 repast-32.png tmp.gif
convert -colors 256 tmp.gif repast-32-8bpp.png
convert -colors 256 repast-48.png tmp.gif
convert -colors 256 tmp.gif repast-48-8bpp.png

#convert repast-256.bmp repast-256.png


icotool -c repast-16-8bpp.png repast-16.png repast-32-8bpp.png repast-32.png repast-48-8bpp.png repast-48.png repast-256.png > repast.ico

rm tmp.gif
rm repast-16-8bpp.png
rm repast-32-8bpp.png
rm repast-48-8bpp.png

