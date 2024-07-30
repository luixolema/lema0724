Considerations:
 * The checkout date will be considered as the first (inclusive) day in the rental agreement, therefore the checkout date is counted as 1 day.
   - example: checoutDate = 7/29/2024, rentalDayCount = 1, dueDate= 7/29/2024 (same date)
   - example: checoutDate = 7/29/2024, rentalDayCount = 2, dueDate= 7/30/2024 (2 days 2 checked dates: 7/29/2024, 7/30/2024)
   - There are some comments about it in the code.
* There is no API security implementation
  - not part of the requirements, and could be done with too many solutions (easy suggestion: cognito library: aws-java-sdk-cognitoidp)
* No API documentation
  - not part of the requirements, (easy suggestion: springdoc-openapi-ui)
* No additional profile or database for testing
  - H2 db is used and populated as example data, which is also commented out in integration tests
* Most of the integration tests could be tested as unit tests in the main method of the service.
  - I just wanted to provide a "solve all test cases in 1 file" so that validation errors (controller layer) are also tested in the same file.
  
Postman testing:
https://www.postman.com/spacecraft-saganist-38434058/workspace/cardinaltest/collection/21760723-4b5697af-5e61-4680-8c4e-d354c03b65c5?action=share&creator=21760723
