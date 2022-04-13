Feature: Pet Shop Catalog

  Scenario: Fetching pets
    Given the following pets are available in the store:
      | name            | description    | type | breed      | price   |
      | Captain Patches | No comment     | Cat  | Moggy      | $200    |
      | The Flash       | Very fast      | Cat  | Mutant     | $10000  |
      | Krypto          | From Krypton   | Dog  | Kryptonian | $100000 |
      | Nemo            | Friend of Dori | Fish | Goldfish   | $15     |
    When I select "Nemo"
    Then the following data should be returned:
      | name | description    | type | breed    | price |
      | Nemo | Friend of Dori | Fish | Goldfish | $15   |
#    add uuids? + add all CRUD ops