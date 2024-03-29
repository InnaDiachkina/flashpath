# Flashpath

Flashpath is an application designed to shorten long URLs 
into concise and convenient links.

## Key Features

- Shortening long URLs into concise and convenient links.
- Storing pairs of short and original URLs in MongoDB and Redis databases.
- Quickly retrieving the original URL based on a short link.

## Installation and Usage

- Clone the Flashpath repository to your local computer.
- Build the project and run it using Maven.
- After a successful installation and launch, the application will be accessible
at http://localhost:8080 

## Usage Examples

#### To obtain a short link, send a POST request to /api/url/flash
- Example : http://localhost:8080/api/url/flash
- Request JSON : {"originalUrl":"http://test.com.test"}
- Response JSON : {"shortUrl":"http://localhost:8080/api/url/original/abcdefj"}
 
#### To retrieve the original URL, send a GET request to /api/url/original
- Example : http://localhost:8080/api/url/original/ + short link (abcdefj)
- Response : Redirect to the original URL (http://test.com.test)