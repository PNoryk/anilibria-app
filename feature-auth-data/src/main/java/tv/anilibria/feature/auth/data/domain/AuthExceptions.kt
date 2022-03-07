package tv.anilibria.feature.domain.errors

class AlreadyAuthorizedException(message: String) : RuntimeException(message)
class EmptyFieldException(message: String) : RuntimeException(message)
class WrongUserAgentException(message: String) : RuntimeException(message)
class InvalidUserException(message: String) : RuntimeException(message)
class Wrong2FaCodeException(message: String) : RuntimeException(message)
class WrongPasswordException(message: String) : RuntimeException(message)

class SocialAuthException(message: String) : RuntimeException(message)

class OtpNotFoundException(message: String) : RuntimeException(message)
class OtpAcceptedException(message: String) : RuntimeException(message)
class OtpNotAcceptedException(message: String) : RuntimeException(message)

