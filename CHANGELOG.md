# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.1] - 2024-06-01

## Added

### Now, support for OpenAPI 3.1 is included. These are some of the new changes:

- Introduces support for webhooks, allowing you to describe asynchronous notifications that your API can send to users' systems.
- Replaces the nullable keyword with type arrays, aligning with JSON Schema by allowing multiple types in the type keyword.
- ExclusiveMinimum and ExclusiveMaximum now take distinct values instead of boolean, simplifying their use.
- Replace example with examples inside schema objects, allowing multiple examples in a YAML array format, aligning with JSON Schema.
- Binary file uploads in POST requests no longer require a schema definition, simplifying the process.
- For base64 encoded file uploads, OpenAPI 3.1 uses contentEncoding to specify the encoding.
- For multipart file uploads with binary files, OpenAPI 3.1 uses contentMediaType to specify the media type.
- The $schema keyword is now allowed in OpenAPI 3.1, enabling the definition of the JSON Schema dialect a model uses, which can be different drafts or custom dialects.
- Introduces support for mutual TLS (mTLS), providing two-way authentication between the client and server for enhanced security.
- General improvements in the alignment and clarity of the specification, including enhancements to OAuth 2.0 support.
- Added support for the identifier keyword in the License Object, allowing the definition of an SPDX license expression for the API.
- Added compatibility with the dosonarapi plugin(now, renamed to SonarOpenAPI-Rules), which now provides many rule definitions for OpenAPI 3.1
 
