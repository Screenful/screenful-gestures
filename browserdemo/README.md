BrowserDemo

Simple browserdemo for testing Server Sent Events (SSE).
- Prints latest SSE.
- Changes every fifth second the picture (the number) to another picture with smaller number.

Might need Java EE base -plugin for Netbeans.

Web page:

http://localhost:8080/Demo/browserDemo.html

Event source:

http://localhost:8080/Demo/DemoServlet

Messages supported:

data: left

- changes to picture with "smaller number".

data: right

- changes to picture with "bigger number".

Source:

- http://milestonenext.blogspot.fi/2013/07/html5-server-sent-events-sample-with.html
