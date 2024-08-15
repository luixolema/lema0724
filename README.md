Considerations:
 * The checkout date will be considered as the first (inclusive) day in the rental agreement, therefore the checkout date is counted as 1 day.
   - example: checoutDate = 7/29/2024, rentalDayCount = 1, dueDate= 7/29/2024 (same date)
   - example: checoutDate = 7/29/2024, rentalDayCount = 2, dueDate= 7/30/2024 (2 days 2 checked dates: 7/29/2024, 7/30/2024)
   - Why?: if the price is based on a daily charge and that changes depending on the day of the week  etc (not in hours). then is not trivial to decide whether to charge for 1 day when the client comes to the store on a Friday and asks for a 1-day rental period. Should the system charge Friday or Saturday prices or a combination of both, what does it depend on? It's easier and more clear to start charging (by days) counting including the checkout day. Having this in mind the consumer system (UI or other service) could send the correct starting counting date for each case. 
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
