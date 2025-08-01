package users.management.service;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import users.management.entity.User;
import users.management.entity.VerificationToken;
import users.management.exception.NotFoundException;
import users.management.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class VerificationTokenService {

    private final int tokenValidityHours;
    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    public String create(User user) {
        UUID token = UUID.randomUUID();
        VerificationToken verificationToken = new VerificationToken(token.toString(), LocalDateTime.now().plusHours(tokenValidityHours), user);
        verificationTokenRepository.save(verificationToken);
        return token.toString();
    }

    @Transactional
    public boolean isValidToken(String token, UUID userID) {
        VerificationToken verificationToken = verificationTokenRepository.findByTokenAndUserId(token, userID)
                .orElseThrow(() -> new NotFoundException(String.format("Verification token %s for user %s not found", token, userID)));
        return LocalDateTime.now().isBefore(verificationToken.getExpiryDate());
    }

}
