Feature: A Domain Specific Language for Stochastic Petri Nets

  Scenario: I want to create a SPN of one transition with two places
    Given the input "from(\"A\") to \"B\""
    When I create the SPN
    Then I should obtain a SPN with 1 transitions
    And the transition is "A" to "B" with markovian rate 1

  Scenario: I want to create a SPN of one transition with markovian rate 0.5
    Given the input "from(\"A\") to \"B\" withRate 0.5"
    When I create the SPN
    Then I should obtain a SPN with 1 transitions
    And the transition is "A" to "B" with markovian rate 0.5

  Scenario: I want to create a SPN of one transition with an inhibitor arc
    Given the input "from(\"A\") to \"B\" inhibitedBy \"C\""
    When I create the SPN
    Then I should obtain a SPN with 1 transitions
    And the transition is "A" to "B" with markovian rate 1
    And inhibited by "C"

  Scenario: I want to create a SPN of one transition consuming multiple tokens
    Given the input "from(\"A\", \"A\", \"A\", \"B\") to \"B\""
    When I create the SPN
    Then I should obtain a SPN with 1 transitions
    And the transition consumes 3 tokens from "A"
    And the transition consumes 1 tokens from "B"

  Scenario: I want to create a SPN with one transition producing multiple tokens
    Given the input "from(\"A\") to (\"B\", \"B\", \"B\", \"A\")"
    When I create the SPN
    Then I should obtain a SPN with 1 transitions
    And the transition produces 3 tokens in "B"
    And the transition produces 1 tokens in "A"

  Scenario: I want to create a SPN with multiple transitions
    Given the input "(from(\"A\") to \"B\") ++ (from(\"B\") to \"C\" withRate 2.5) ++ (from(\"A\", \"B\") to \"D\" inhibitedBy \"E\")"
    When I create the SPN
    Then I should obtain a SPN with 3 transitions

  Scenario: I want to create a SPN with wrong input
    Given the input "from(\"A\") to \"A\", \"B\""
    When I create the SPN
    Then I should obtain an IllegalArgumentException

