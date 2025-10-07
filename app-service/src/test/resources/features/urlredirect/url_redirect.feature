Feature: URL redirect

  Scenario: resolve existing code to redirect url
    Given a short code "abc123"
    And repository has mapping code "abc123" to long url "https://example.com/known"
    When redirect is requested
    Then redirect url should be "https://example.com/known"

  Scenario: code not found -> throws business validation
    Given a short code "notfound"
    And repository has no mapping for that code
    When redirect is requested
    Then a business validation error should be thrown with code "AVR-X0006"


