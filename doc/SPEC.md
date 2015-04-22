Create a web-based word counting  service, using Ruby/Rails or another web app framework or language.

There should be an index page, a form page, and a result/form response page. If you find it more natural, the form and index page can be combined.

The input form should request a URL as the only field, with some amount of validation.

The result/form response page should:
* Check to see if this URL has previously been scanned,  If so, display the stored results from the previous scan (see below).
* Otherwise, read the URL and provide a list of the 10 most common words that appear as text (i.e. visibly, but not an image) at that URL, along with the number of times they appear in the text. Store the result for subsequent display (see above).

The index page should list the previously scanned URLs, the most common  meaningful word at the URL and the number of times it appeared, and a way to get to the result page for that particular URL.

We are not looking for a designer, but a bit of CSS to make the pages look better than the default would be appreciated! Other things we will evaluate will include: the economy of your code and the quality of the result (ideally, proven with tests)

Please deliver your code in the form of a tarball containing a directory or a Git repository. A link to a public repository will also work. If not a Rails app, please provide instructions to start the application, as we will be inspecting both the code and its behavior.