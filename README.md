# TabJpeg
Java front end to cjpeg

This was started because no major grapical app that I used had an easy way to integrate MozJpeg, so I made my own. Yes, this is in Java. If I got Emscripten to work, I swear this would be in JavaScript.

## Requirements
* Java 8
* ```cjpeg``` binary

run with ```javaw -jar TabJpeg.jar```, or TabJpeg.bat on Windows.

```cjpeg``` must be in the current directory, or in PATH. Compile from source at https://github.com/mozilla/mozjpeg or be lazy and get binaries directly from http://mozjpeg.codelove.de/binaries.html

This uses the Java ImageIO library to do some image processing, so most ubiquitous image formats should be supported (BMP, PNG, JPG). Built with Netbeans.

## Features
* Live preview
* JPEG quality control
* Chroma downsampling
* Resizing
 
## Known bugs
* Doesn't load images bigger than about 3 megapixels (1080p screenshots OK)
* Gamma incorrect resizing


