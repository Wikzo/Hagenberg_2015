XConsole
===
XConsole is a single file console system for XNA Game Studio 4.0 games. XConsole is a singleton class that is also a game component meaning it's incredibly easy to add to your game. The only other requirement for XConsole is that you implement and register the IKeyboardInputService which provides XConsole information about the keyboard input. The demo app includes one such implementation, but you can easily replace that with any implementation of your choosing.

![Demo App Screenshot](https://bitbucket.org/nickgravelyn/xconsole/wiki/Images/demoapp.png)

Getting Started
---------------
XConsole is pretty simple to get running. The first dependency is that you register an IKeyboardInputService with the game's Services:

    // in the game constructor
    Services.AddService(typeof(XConsole.IKeyboardInputService), keyboardService);

The demo has the game itself implement XConsole.IKeyboardInputService, but you can feel free to replace it with any implementation you want. It just has to be there before XConsole.

Once you have that in place, you can add XConsole to the Components collection:

    // Initialize the XConsole. We must use Content/ in the font because it is relative to the executable.
    Components.Add(new XConsole(Services, "Content/ConsoleFont"));

Since XConsole is a singleton, you don't have to store a reference to it if you don't want as you can always use XConsole.Instance to get a reference to it later.

Now you're free to use any of the methods on XConsole to register commands or write output. By default XConsole is tied to the tilde (~) key of the keyboard, but there is a property you can set if you wish to change that.

For more information view the [wiki](https://bitbucket.org/nickgravelyn/xconsole/wiki).

License
-------
Copyright (C) 2011 by Nick Gravelyn

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.