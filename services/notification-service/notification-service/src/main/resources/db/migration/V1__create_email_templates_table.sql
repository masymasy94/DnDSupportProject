-- Email templates table for storing HTML email templates
CREATE TABLE IF NOT EXISTS email_templates (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    subject VARCHAR(255),
    html_content TEXT NOT NULL,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for better performance
CREATE INDEX IF NOT EXISTS idx_email_templates_name ON email_templates(name);
CREATE INDEX IF NOT EXISTS idx_email_templates_active ON email_templates(active);

-- Insert default welcome email template for new user registration
INSERT INTO email_templates (name, subject, html_content, description, active)
VALUES (
    'welcome-new-user',
    'Welcome to D&D Platform, {{username}}!',
    '<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;">
    <table role="presentation" width="100%" cellspacing="0" cellpadding="0" style="max-width: 600px; margin: 0 auto; background-color: #ffffff;">
        <tr>
            <td style="padding: 40px 30px; text-align: center; background-color: #7c3aed;">
                <h1 style="color: #ffffff; margin: 0; font-size: 28px;">Welcome, Adventurer!</h1>
            </td>
        </tr>
        <tr>
            <td style="padding: 40px 30px;">
                <h2 style="color: #333333; margin: 0 0 20px 0;">Hello {{username}},</h2>
                <p style="color: #666666; line-height: 1.6; margin: 0 0 20px 0;">
                    Your journey begins now! Thank you for joining the D&D Platform. We are thrilled to have you as part of our community of adventurers.
                </p>
                <p style="color: #666666; line-height: 1.6; margin: 0 0 20px 0;">
                    With your new account, you can:
                </p>
                <ul style="color: #666666; line-height: 1.8; margin: 0 0 20px 0; padding-left: 20px;">
                    <li>Create and manage your characters</li>
                    <li>Join campaigns with fellow adventurers</li>
                    <li>Track your quests and achievements</li>
                    <li>Connect with other players</li>
                </ul>
                <p style="color: #666666; line-height: 1.6; margin: 0 0 30px 0;">
                    Ready to start your adventure?
                </p>
                <table role="presentation" cellspacing="0" cellpadding="0" style="margin: 0 auto;">
                    <tr>
                        <td style="background-color: #7c3aed; border-radius: 5px;">
                            <a href="{{loginUrl}}" style="display: inline-block; padding: 15px 30px; color: #ffffff; text-decoration: none; font-weight: bold;">Start Your Adventure</a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td style="padding: 30px; text-align: center; background-color: #f8f8f8; border-top: 1px solid #eeeeee;">
                <p style="color: #999999; font-size: 12px; margin: 0;">
                    This email was sent to {{email}}. If you did not create an account, please ignore this email.
                </p>
            </td>
        </tr>
    </table>
</body>
</html>',
    'Welcome email template sent to newly registered users',
    true
);
