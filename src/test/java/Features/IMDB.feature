Feature: Search

  Scenario Outline: Search a movie in IMDB
    Given I am in IMDB page
    When I select "<option>"
    And I enter a <movie>
    Then I could see the result for the <movie>
    Examples:
      | option      | movie        |
      | Titles      | it           |
      | TV Episodes | it           |
      | Celebs      | it           |
      | Companies   | it           |
      | Keywords    | it           |
