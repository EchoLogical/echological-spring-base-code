Feature: Generate short url

  Scenario: return existing short url for known long url
    Given a long url "https://example.com/known"
    And repository has existing short url "abc123" for that long url
    When generate short url with code length 6 and code loop 1
    Then result short url should be "abc123"
    And url path should not be empty

  Scenario: generate new short url when not exist and no collision
    Given a long url "https://example.com/new"
    And repository has no existing data for that long url
    And next generated random codes are "xy12zz"
    When generate short url with code length 6 and code loop 1
    Then result short url should be "xy12zz"
    And url path should not be empty

  Scenario: fail after max collision
    Given a long url "https://example.com/collide"
    And repository has no existing data for that long url
    And next generated random codes are "dup001","dup001"
    When generate short url with code length 6 and code loop 1
    Then a business validation error should be thrown with code "AVR-X0001"


