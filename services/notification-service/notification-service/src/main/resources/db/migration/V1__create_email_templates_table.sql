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
    'Welcome to D&D Platform - Your Adventure Awaits!',
    '<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body style="margin: 0; padding: 0; font-family: ''Segoe UI'', Arial, sans-serif; background-color: #1a1a2e;">
    <table role="presentation" width="100%" cellspacing="0" cellpadding="0" style="max-width: 600px; margin: 0 auto;">
        <!-- Header with gradient -->
        <tr>
            <td style="padding: 0;">
                <table role="presentation" width="100%" cellspacing="0" cellpadding="0" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 12px 12px 0 0;">
                    <tr>
                        <td style="padding: 50px 40px; text-align: center;">
                            <div style="font-size: 48px; margin-bottom: 15px;">🐉</div>
                            <h1 style="color: #ffffff; margin: 0; font-size: 32px; font-weight: 700; text-shadow: 2px 2px 4px rgba(0,0,0,0.3);">Welcome, Adventurer!</h1>
                            <p style="color: rgba(255,255,255,0.9); margin: 15px 0 0 0; font-size: 16px;">Your epic journey is about to begin</p>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Main content -->
        <tr>
            <td style="background-color: #ffffff; padding: 40px;">
                <p style="color: #333333; font-size: 16px; line-height: 1.7; margin: 0 0 25px 0;">
                    Thank you for joining the D&D Platform! We are thrilled to welcome you to our community of adventurers, storytellers, and heroes.
                </p>

                <p style="color: #333333; font-size: 16px; line-height: 1.7; margin: 0 0 25px 0;">
                    Prepare yourself for unforgettable quests and legendary tales. Here is what awaits you:
                </p>

                <!-- Feature cards -->
                <table role="presentation" width="100%" cellspacing="0" cellpadding="0" style="margin-bottom: 30px;">
                    <tr>
                        <td style="padding: 15px; background-color: #f8f5ff; border-radius: 8px; border-left: 4px solid #7c3aed; margin-bottom: 10px;">
                            <table role="presentation" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td style="padding-right: 15px; vertical-align: top; font-size: 24px;">⚔️</td>
                                    <td>
                                        <strong style="color: #7c3aed; font-size: 15px;">Character Creation</strong>
                                        <p style="color: #666666; margin: 5px 0 0 0; font-size: 14px;">Forge your hero with our intuitive character builder</p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr><td style="height: 10px;"></td></tr>
                    <tr>
                        <td style="padding: 15px; background-color: #f8f5ff; border-radius: 8px; border-left: 4px solid #7c3aed;">
                            <table role="presentation" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td style="padding-right: 15px; vertical-align: top; font-size: 24px;">🗺️</td>
                                    <td>
                                        <strong style="color: #7c3aed; font-size: 15px;">Campaign Management</strong>
                                        <p style="color: #666666; margin: 5px 0 0 0; font-size: 14px;">Join or create campaigns with fellow adventurers</p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr><td style="height: 10px;"></td></tr>
                    <tr>
                        <td style="padding: 15px; background-color: #f8f5ff; border-radius: 8px; border-left: 4px solid #7c3aed;">
                            <table role="presentation" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td style="padding-right: 15px; vertical-align: top; font-size: 24px;">🎲</td>
                                    <td>
                                        <strong style="color: #7c3aed; font-size: 15px;">Combat Tracker</strong>
                                        <p style="color: #666666; margin: 5px 0 0 0; font-size: 14px;">Manage battles with our streamlined combat system</p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>

                <!-- CTA Button -->
                <table role="presentation" width="100%" cellspacing="0" cellpadding="0">
                    <tr>
                        <td style="text-align: center; padding: 10px 0 20px 0;">
                            <a href="https://dndplatform.com" style="display: inline-block; padding: 16px 40px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: #ffffff; text-decoration: none; font-weight: 600; font-size: 16px; border-radius: 8px; box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);">Begin Your Quest</a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <!-- Footer -->
        <tr>
            <td style="background-color: #2d2d44; padding: 30px 40px; border-radius: 0 0 12px 12px;">
                <table role="presentation" width="100%" cellspacing="0" cellpadding="0">
                    <tr>
                        <td style="text-align: center;">
                            <p style="color: #a0a0b0; font-size: 14px; margin: 0 0 10px 0;">
                                May your rolls be natural 20s!
                            </p>
                            <p style="color: #6b6b80; font-size: 12px; margin: 0;">
                                D&D Platform Team
                            </p>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</body>
</html>',
    'Welcome email template sent to newly registered users',
    true
);
