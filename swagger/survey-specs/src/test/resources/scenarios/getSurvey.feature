Feature: Get a Survey

  Background:
    Given there is a Surveys server

  Scenario: get a known survey with valid URL
    Given I have a survey with the mandatory properties set
    And I know a survey id
    When I GET it from the /survey/ID endpoint
    Then I receive a 200 status code
    And I receive the correct survey

  Scenario: get a survey with valid URL not created
    Given I know an id that doesn't match any survey
    When I GET it from the /survey/ID endpoint
    Then I receive a 404 status code