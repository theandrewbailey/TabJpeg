# TabJpeg
Java front end to cjpeg

This was started because no major grapical app that I used had an easy way to integrated MozJpeg into it, so I made my own. Yes, this is in Java. If I got Emscripten to work, I swear this would be in JavaScript.

Requirements:
* Java 8
* ```cjpeg``` binary

run by invoking ```javaw -jar TabJpeg.jar```, or TabJpeg.bat on Windows.

```cjpeg``` must be in your current directory, or in your PATH. Compile from source at https://github.com/mozilla/mozjpeg or be lazy and get binaries directly from http://mozjpeg.codelove.de/binaries.html

This uses the Java ImageIO library to do some image processing, so most ubiquitous image formats should be supported (BMP, PNG, JPG). Built with Netbeans.

