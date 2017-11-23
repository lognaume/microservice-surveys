Feature: Creation of Survey

  Background:
    Given there is a Surveys server

  Scenario: create a survey
    Given I have an empty survey payload
    When I POST it to the /survey endpoint
    Then I receive a 400 status code

  Scenario: create a survey
    Given I have a survey with an owner payload
    When I POST it to the /survey endpoint
    Then I receive a 201 status code

  Scenario: create a wrong content type survey
    Given I have a wrong content type survey payload
    When I custom POST it to the /survey endpoint
    Then I receive a 415 status code

  Scenario: create a wrong body survey
    Given I have a wrong body survey payload
    When I custom POST it to the /survey endpoint
    Then I receive a 400 status code

  Scenario: create a wrong owner survey
    Given I have a wrong owner survey payload
    When I custom POST it to the /survey endpoint
    Then I receive a 400 status code

  Scenario: create a wrong title survey
    Given I have a int title survey payload
    When I custom POST it to the /survey endpoint
    Then I receive a 201 status code

  Scenario: create a wrong description survey
    Given I have a int description survey payload
    When I custom POST it to the /survey endpoint
    Then I receive a 201 status code

  Scenario: create a wrong created survey
    Given I have a int created survey payload
    When I custom POST it to the /survey endpoint
    Then I receive a 201 status code

  Scenario: create a wrong questions survey
    Given I have a int questions survey payload
    When I custom POST it to the /survey endpoint
    Then I receive a 201 status code