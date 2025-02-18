import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        String credentialsToHash = username + ":" + password;
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(salt);
        byte[] hashedCredsAsBytes = messageDigest.digest(credentialsToHash.getBytes(StandardCharsets.UTF_8));

        String saltHex = bytesToHex(salt);
        String hashedCredsHex = bytesToHex(hashedCredsAsBytes);

        Cookie accountCookie = new Cookie("auth", saltHex + ":" + hashedCredsHex);
        response.addCookie(accountCookie);

        response.sendRedirect(request.getContextPath()+"/bank/main.jsp");
    } catch (Exception ex) {
        ex.printStackTrace();
        response.sendError(500);
    }

    return;
}

private String bytesToHex(byte[] bytes) {
    StringBuilder hexString = new StringBuilder();
    for (byte b : bytes) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
    }
    return hexString.toString();
}