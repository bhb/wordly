# wordly

Counting the visibible words on websites since 2015.

**IMPORTANT** : The data storage is currently in-memory, so if you restart the server, you will lose all cached counts.

## Prerequisites

Install Homebrew:

    ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

Install Lein

    brew install leinengen

## Running

To start a web server for the application, run:

    lein ring server

To run the tests (note that some integration tests require an internet connection)

    lein test

## Future improvements

* Check words against a dictionary to make sure they are real
* Fetch HTML from URL asynchronously

## License

Copyright © 2015 Ben Brinckerhoff
