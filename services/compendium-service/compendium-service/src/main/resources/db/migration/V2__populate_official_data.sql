-- =============================================
-- Populate D&D 5E Official Reference Data
-- =============================================

-- Alignments (9)
INSERT INTO alignments (code, name) VALUES
    ('LG', 'Lawful Good'),
    ('NG', 'Neutral Good'),
    ('CG', 'Chaotic Good'),
    ('LN', 'Lawful Neutral'),
    ('TN', 'True Neutral'),
    ('CN', 'Chaotic Neutral'),
    ('LE', 'Lawful Evil'),
    ('NE', 'Neutral Evil'),
    ('CE', 'Chaotic Evil');

-- Abilities (6)
INSERT INTO abilities (code, name) VALUES
    ('STR', 'Strength'),
    ('DEX', 'Dexterity'),
    ('CON', 'Constitution'),
    ('INT', 'Intelligence'),
    ('WIS', 'Wisdom'),
    ('CHA', 'Charisma');

-- Skills (18) - must be inserted after abilities
INSERT INTO skills (name, ability_id) VALUES
    ('Athletics', (SELECT id FROM abilities WHERE code = 'STR')),
    ('Acrobatics', (SELECT id FROM abilities WHERE code = 'DEX')),
    ('Sleight of Hand', (SELECT id FROM abilities WHERE code = 'DEX')),
    ('Stealth', (SELECT id FROM abilities WHERE code = 'DEX')),
    ('Arcana', (SELECT id FROM abilities WHERE code = 'INT')),
    ('History', (SELECT id FROM abilities WHERE code = 'INT')),
    ('Investigation', (SELECT id FROM abilities WHERE code = 'INT')),
    ('Nature', (SELECT id FROM abilities WHERE code = 'INT')),
    ('Religion', (SELECT id FROM abilities WHERE code = 'INT')),
    ('Animal Handling', (SELECT id FROM abilities WHERE code = 'WIS')),
    ('Insight', (SELECT id FROM abilities WHERE code = 'WIS')),
    ('Medicine', (SELECT id FROM abilities WHERE code = 'WIS')),
    ('Perception', (SELECT id FROM abilities WHERE code = 'WIS')),
    ('Survival', (SELECT id FROM abilities WHERE code = 'WIS')),
    ('Deception', (SELECT id FROM abilities WHERE code = 'CHA')),
    ('Intimidation', (SELECT id FROM abilities WHERE code = 'CHA')),
    ('Performance', (SELECT id FROM abilities WHERE code = 'CHA')),
    ('Persuasion', (SELECT id FROM abilities WHERE code = 'CHA'));

-- Species/Races (D&D 5E 2024 PHB core races)
INSERT INTO species (name, description, source) VALUES
    ('Aasimar', 'Celestial heritage with divine power', 'OFFICIAL'),
    ('Dragonborn', 'Draconic humanoids with breath weapons', 'OFFICIAL'),
    ('Dwarf', 'Stout folk known for craftsmanship', 'OFFICIAL'),
    ('Elf', 'Graceful people with keen senses', 'OFFICIAL'),
    ('Gnome', 'Small, clever inventors and illusionists', 'OFFICIAL'),
    ('Goliath', 'Towering warriors of the mountains', 'OFFICIAL'),
    ('Halfling', 'Small, nimble, and lucky folk', 'OFFICIAL'),
    ('Human', 'Versatile and ambitious people', 'OFFICIAL'),
    ('Orc', 'Fierce warriors with primal strength', 'OFFICIAL'),
    ('Tiefling', 'Infernal heritage with fiendish traits', 'OFFICIAL');

-- Character Classes (13)
INSERT INTO character_classes (name, hit_die, description, source) VALUES
    ('Barbarian', 'd12', 'Fierce warriors who channel primal rage', 'OFFICIAL'),
    ('Bard', 'd8', 'Inspiring performers and jack-of-all-trades', 'OFFICIAL'),
    ('Cleric', 'd8', 'Divine spellcasters serving their deity', 'OFFICIAL'),
    ('Druid', 'd8', 'Nature magic wielders and shapeshifters', 'OFFICIAL'),
    ('Fighter', 'd10', 'Masters of martial combat', 'OFFICIAL'),
    ('Monk', 'd8', 'Martial artists harnessing ki energy', 'OFFICIAL'),
    ('Paladin', 'd10', 'Holy warriors bound by sacred oaths', 'OFFICIAL'),
    ('Ranger', 'd10', 'Skilled hunters and trackers', 'OFFICIAL'),
    ('Rogue', 'd8', 'Stealthy experts in precise strikes', 'OFFICIAL'),
    ('Sorcerer', 'd6', 'Innate spellcasters with raw magic', 'OFFICIAL'),
    ('Warlock', 'd8', 'Spellcasters empowered by patrons', 'OFFICIAL'),
    ('Wizard', 'd6', 'Scholarly masters of arcane magic', 'OFFICIAL'),
    ('Artificer', 'd8', 'Inventors who infuse magic into items', 'OFFICIAL');

-- Backgrounds (13 classic backgrounds)
INSERT INTO backgrounds (name, description, source) VALUES
    ('Acolyte', 'Served in a temple, gaining religious knowledge', 'OFFICIAL'),
    ('Charlatan', 'Master of deception and false identities', 'OFFICIAL'),
    ('Criminal', 'Experienced in breaking the law', 'OFFICIAL'),
    ('Entertainer', 'Performer who thrives in the spotlight', 'OFFICIAL'),
    ('Folk Hero', 'Common person who became a local legend', 'OFFICIAL'),
    ('Guild Artisan', 'Member of an artisan guild', 'OFFICIAL'),
    ('Hermit', 'Lived in seclusion, seeking enlightenment', 'OFFICIAL'),
    ('Noble', 'Born into wealth and privilege', 'OFFICIAL'),
    ('Outlander', 'Grew up in the wilds, far from civilization', 'OFFICIAL'),
    ('Sage', 'Dedicated scholar and researcher', 'OFFICIAL'),
    ('Sailor', 'Experienced on ships and the sea', 'OFFICIAL'),
    ('Soldier', 'Trained military combatant', 'OFFICIAL'),
    ('Urchin', 'Grew up on the streets, surviving by wits', 'OFFICIAL');

-- Proficiency Types (4)
INSERT INTO proficiency_types (name) VALUES
    ('ARMOR'),
    ('WEAPON'),
    ('TOOL'),
    ('LANGUAGE');

-- Armor Types (4)
INSERT INTO armor_types (name) VALUES
    ('Light Armor'),
    ('Medium Armor'),
    ('Heavy Armor'),
    ('Shields');

-- Weapon Types - Simple Weapons
INSERT INTO weapon_types (name, category) VALUES
    ('Simple Weapons', 'SIMPLE'),
    ('Club', 'SIMPLE'),
    ('Dagger', 'SIMPLE'),
    ('Greatclub', 'SIMPLE'),
    ('Handaxe', 'SIMPLE'),
    ('Javelin', 'SIMPLE'),
    ('Light Hammer', 'SIMPLE'),
    ('Mace', 'SIMPLE'),
    ('Quarterstaff', 'SIMPLE'),
    ('Sickle', 'SIMPLE'),
    ('Spear', 'SIMPLE'),
    ('Light Crossbow', 'SIMPLE'),
    ('Dart', 'SIMPLE'),
    ('Shortbow', 'SIMPLE'),
    ('Sling', 'SIMPLE');

-- Weapon Types - Martial Weapons
INSERT INTO weapon_types (name, category) VALUES
    ('Martial Weapons', 'MARTIAL'),
    ('Battleaxe', 'MARTIAL'),
    ('Flail', 'MARTIAL'),
    ('Glaive', 'MARTIAL'),
    ('Greataxe', 'MARTIAL'),
    ('Greatsword', 'MARTIAL'),
    ('Halberd', 'MARTIAL'),
    ('Lance', 'MARTIAL'),
    ('Longsword', 'MARTIAL'),
    ('Maul', 'MARTIAL'),
    ('Morningstar', 'MARTIAL'),
    ('Pike', 'MARTIAL'),
    ('Rapier', 'MARTIAL'),
    ('Scimitar', 'MARTIAL'),
    ('Shortsword', 'MARTIAL'),
    ('Trident', 'MARTIAL'),
    ('War Pick', 'MARTIAL'),
    ('Warhammer', 'MARTIAL'),
    ('Whip', 'MARTIAL'),
    ('Blowgun', 'MARTIAL'),
    ('Hand Crossbow', 'MARTIAL'),
    ('Heavy Crossbow', 'MARTIAL'),
    ('Longbow', 'MARTIAL'),
    ('Net', 'MARTIAL');

-- Tool Types - Artisan's Tools
INSERT INTO tool_types (name, category) VALUES
    ('Alchemist''s Supplies', 'ARTISAN'),
    ('Brewer''s Supplies', 'ARTISAN'),
    ('Calligrapher''s Supplies', 'ARTISAN'),
    ('Carpenter''s Tools', 'ARTISAN'),
    ('Cartographer''s Tools', 'ARTISAN'),
    ('Cobbler''s Tools', 'ARTISAN'),
    ('Cook''s Utensils', 'ARTISAN'),
    ('Glassblower''s Tools', 'ARTISAN'),
    ('Jeweler''s Tools', 'ARTISAN'),
    ('Leatherworker''s Tools', 'ARTISAN'),
    ('Mason''s Tools', 'ARTISAN'),
    ('Painter''s Supplies', 'ARTISAN'),
    ('Potter''s Tools', 'ARTISAN'),
    ('Smith''s Tools', 'ARTISAN'),
    ('Tinker''s Tools', 'ARTISAN'),
    ('Weaver''s Tools', 'ARTISAN'),
    ('Woodcarver''s Tools', 'ARTISAN');

-- Tool Types - Gaming Sets
INSERT INTO tool_types (name, category) VALUES
    ('Dice Set', 'GAMING'),
    ('Playing Card Set', 'GAMING'),
    ('Dragonchess Set', 'GAMING'),
    ('Three-Dragon Ante Set', 'GAMING');

-- Tool Types - Musical Instruments
INSERT INTO tool_types (name, category) VALUES
    ('Bagpipes', 'INSTRUMENT'),
    ('Drum', 'INSTRUMENT'),
    ('Dulcimer', 'INSTRUMENT'),
    ('Flute', 'INSTRUMENT'),
    ('Lute', 'INSTRUMENT'),
    ('Lyre', 'INSTRUMENT'),
    ('Horn', 'INSTRUMENT'),
    ('Pan Flute', 'INSTRUMENT'),
    ('Shawm', 'INSTRUMENT'),
    ('Viol', 'INSTRUMENT');

-- Tool Types - Other Tools
INSERT INTO tool_types (name, category) VALUES
    ('Disguise Kit', 'OTHER'),
    ('Forgery Kit', 'OTHER'),
    ('Herbalism Kit', 'OTHER'),
    ('Navigator''s Tools', 'OTHER'),
    ('Poisoner''s Kit', 'OTHER'),
    ('Thieves'' Tools', 'OTHER'),
    ('Vehicles (Land)', 'OTHER'),
    ('Vehicles (Water)', 'OTHER');

-- Languages - Standard
INSERT INTO languages (name, script, type) VALUES
    ('Common', 'Common', 'STANDARD'),
    ('Dwarvish', 'Dwarvish', 'STANDARD'),
    ('Elvish', 'Elvish', 'STANDARD'),
    ('Giant', 'Dwarvish', 'STANDARD'),
    ('Gnomish', 'Dwarvish', 'STANDARD'),
    ('Goblin', 'Dwarvish', 'STANDARD'),
    ('Halfling', 'Common', 'STANDARD'),
    ('Orc', 'Dwarvish', 'STANDARD');

-- Languages - Exotic
INSERT INTO languages (name, script, type) VALUES
    ('Abyssal', 'Infernal', 'EXOTIC'),
    ('Celestial', 'Celestial', 'EXOTIC'),
    ('Draconic', 'Draconic', 'EXOTIC'),
    ('Deep Speech', NULL, 'EXOTIC'),
    ('Infernal', 'Infernal', 'EXOTIC'),
    ('Primordial', 'Dwarvish', 'EXOTIC'),
    ('Sylvan', 'Elvish', 'EXOTIC'),
    ('Undercommon', 'Elvish', 'EXOTIC');

-- =============================================
-- Spellcasting Reference Data
-- =============================================

-- Spell Schools (8 schools of magic)
INSERT INTO spell_schools (name) VALUES
    ('Abjuration'),
    ('Conjuration'),
    ('Divination'),
    ('Enchantment'),
    ('Evocation'),
    ('Illusion'),
    ('Necromancy'),
    ('Transmutation');

-- Core D&D 5E Spells (cantrips and common spells)
-- Cantrips (Level 0)
INSERT INTO spells (name, level, school_id, casting_time, spell_range, components, duration, concentration, ritual, description, source) VALUES
    ('Acid Splash', 0, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '60 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'You hurl a bubble of acid.', 'OFFICIAL'),
    ('Blade Ward', 0, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Self', 'V, S', '1 round', FALSE, FALSE, 'Resistance to bludgeoning, piercing, and slashing damage.', 'OFFICIAL'),
    ('Chill Touch', 0, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', '120 feet', 'V, S', '1 round', FALSE, FALSE, 'A ghostly, skeletal hand assails your foe.', 'OFFICIAL'),
    ('Dancing Lights', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '120 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Create up to four torch-sized lights.', 'OFFICIAL'),
    ('Druidcraft', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '30 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'Whispering to the spirits of nature.', 'OFFICIAL'),
    ('Eldritch Blast', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '120 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'A beam of crackling energy streaks toward a creature.', 'OFFICIAL'),
    ('Fire Bolt', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '120 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'You hurl a mote of fire at a creature or object.', 'OFFICIAL'),
    ('Friends', 0, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', 'Self', 'S, M', '1 minute', TRUE, FALSE, 'Gain advantage on Charisma checks.', 'OFFICIAL'),
    ('Guidance', 0, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', 'Touch', 'V, S', '1 minute', TRUE, FALSE, 'Add 1d4 to one ability check.', 'OFFICIAL'),
    ('Light', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Touch', 'V, M', '1 hour', FALSE, FALSE, 'Object sheds bright light in a 20-foot radius.', 'OFFICIAL'),
    ('Mage Hand', 0, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '30 feet', 'V, S', '1 minute', FALSE, FALSE, 'A spectral, floating hand appears.', 'OFFICIAL'),
    ('Mending', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 minute', 'Touch', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'Repair a single break or tear in an object.', 'OFFICIAL'),
    ('Message', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '120 feet', 'V, S, M', '1 round', FALSE, FALSE, 'Whisper a message to a creature.', 'OFFICIAL'),
    ('Minor Illusion', 0, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', '30 feet', 'S, M', '1 minute', FALSE, FALSE, 'Create a sound or an image of an object.', 'OFFICIAL'),
    ('Poison Spray', 0, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '10 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'Project a puff of noxious gas.', 'OFFICIAL'),
    ('Prestidigitation', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '10 feet', 'V, S', '1 hour', FALSE, FALSE, 'Minor magical tricks.', 'OFFICIAL'),
    ('Produce Flame', 0, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', 'Self', 'V, S', '10 minutes', FALSE, FALSE, 'A flickering flame appears in your hand.', 'OFFICIAL'),
    ('Ray of Frost', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '60 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'A frigid beam of blue-white light.', 'OFFICIAL'),
    ('Resistance', 0, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Touch', 'V, S, M', '1 minute', TRUE, FALSE, 'Add 1d4 to one saving throw.', 'OFFICIAL'),
    ('Sacred Flame', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '60 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'Flame-like radiance descends on a creature.', 'OFFICIAL'),
    ('Shillelagh', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 bonus action', 'Touch', 'V, S, M', '1 minute', FALSE, FALSE, 'Imbue a club or quarterstaff with nature''s power.', 'OFFICIAL'),
    ('Shocking Grasp', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Touch', 'V, S', 'Instantaneous', FALSE, FALSE, 'Lightning springs from your hand.', 'OFFICIAL'),
    ('Spare the Dying', 0, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', 'Touch', 'V, S', 'Instantaneous', FALSE, FALSE, 'Stabilize a dying creature.', 'OFFICIAL'),
    ('Thaumaturgy', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '30 feet', 'V', '1 minute', FALSE, FALSE, 'Manifest a minor wonder.', 'OFFICIAL'),
    ('Thorn Whip', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '30 feet', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'A vine-like whip covered in thorns.', 'OFFICIAL'),
    ('True Strike', 0, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', '30 feet', 'S', '1 round', TRUE, FALSE, 'Gain insight into target''s defenses.', 'OFFICIAL'),
    ('Vicious Mockery', 0, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '60 feet', 'V', 'Instantaneous', FALSE, FALSE, 'Unleash a string of insults laced with subtle enchantments.', 'OFFICIAL');

-- 1st Level Spells
INSERT INTO spells (name, level, school_id, casting_time, spell_range, components, duration, concentration, ritual, description, source) VALUES
    ('Alarm', 1, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 minute', '30 feet', 'V, S, M', '8 hours', FALSE, TRUE, 'Set an alarm against unwanted intrusion.', 'OFFICIAL'),
    ('Bless', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '30 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Bless up to three creatures with 1d4 bonus.', 'OFFICIAL'),
    ('Burning Hands', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Self (15-foot cone)', 'V, S', 'Instantaneous', FALSE, FALSE, 'A thin sheet of flames shoots forth from your fingertips.', 'OFFICIAL'),
    ('Charm Person', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '30 feet', 'V, S', '1 hour', FALSE, FALSE, 'Attempt to charm a humanoid.', 'OFFICIAL'),
    ('Cure Wounds', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Touch', 'V, S', 'Instantaneous', FALSE, FALSE, 'A creature you touch regains hit points.', 'OFFICIAL'),
    ('Detect Magic', 1, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', 'Self', 'V, S', '10 minutes', TRUE, TRUE, 'Sense the presence of magic.', 'OFFICIAL'),
    ('Disguise Self', 1, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', 'Self', 'V, S', '1 hour', FALSE, FALSE, 'Change your appearance.', 'OFFICIAL'),
    ('Faerie Fire', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '60 feet', 'V', '1 minute', TRUE, FALSE, 'Objects and creatures are outlined in light.', 'OFFICIAL'),
    ('Feather Fall', 1, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 reaction', '60 feet', 'V, M', '1 minute', FALSE, FALSE, 'Slow the rate of descent for falling creatures.', 'OFFICIAL'),
    ('Find Familiar', 1, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 hour', '10 feet', 'V, S, M', 'Instantaneous', FALSE, TRUE, 'Gain the service of a familiar.', 'OFFICIAL'),
    ('Fog Cloud', 1, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '120 feet', 'V, S', '1 hour', TRUE, FALSE, 'Create a sphere of fog.', 'OFFICIAL'),
    ('Guiding Bolt', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '120 feet', 'V, S', '1 round', FALSE, FALSE, 'A flash of light streaks toward a creature.', 'OFFICIAL'),
    ('Healing Word', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 bonus action', '60 feet', 'V', 'Instantaneous', FALSE, FALSE, 'A creature regains hit points.', 'OFFICIAL'),
    ('Hex', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 bonus action', '90 feet', 'V, S, M', '1 hour', TRUE, FALSE, 'Place a curse on a creature.', 'OFFICIAL'),
    ('Hunter''s Mark', 1, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 bonus action', '90 feet', 'V', '1 hour', TRUE, FALSE, 'Mark a creature as your quarry.', 'OFFICIAL'),
    ('Identify', 1, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 minute', 'Touch', 'V, S, M', 'Instantaneous', FALSE, TRUE, 'Learn the properties of a magic item.', 'OFFICIAL'),
    ('Mage Armor', 1, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Touch', 'V, S, M', '8 hours', FALSE, FALSE, 'Surround a creature with a magical force.', 'OFFICIAL'),
    ('Magic Missile', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '120 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'Create three glowing darts of magical force.', 'OFFICIAL'),
    ('Shield', 1, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 reaction', 'Self', 'V, S', '1 round', FALSE, FALSE, 'An invisible barrier of magical force.', 'OFFICIAL'),
    ('Sleep', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '90 feet', 'V, S, M', '1 minute', FALSE, FALSE, 'Send creatures into a magical slumber.', 'OFFICIAL'),
    ('Thunderwave', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Self (15-foot cube)', 'V, S', 'Instantaneous', FALSE, FALSE, 'A wave of thunderous force sweeps out from you.', 'OFFICIAL');

-- 2nd Level Spells
INSERT INTO spells (name, level, school_id, casting_time, spell_range, components, duration, concentration, ritual, description, source) VALUES
    ('Aid', 2, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', '30 feet', 'V, S, M', '8 hours', FALSE, FALSE, 'Bolster allies with toughness and resolve.', 'OFFICIAL'),
    ('Blur', 2, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', 'Self', 'V', '1 minute', TRUE, FALSE, 'Your body becomes blurred.', 'OFFICIAL'),
    ('Darkness', 2, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '60 feet', 'V, M', '10 minutes', TRUE, FALSE, 'Magical darkness spreads from a point.', 'OFFICIAL'),
    ('Hold Person', 2, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '60 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Paralyze a humanoid.', 'OFFICIAL'),
    ('Invisibility', 2, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', 'Touch', 'V, S, M', '1 hour', TRUE, FALSE, 'A creature becomes invisible.', 'OFFICIAL'),
    ('Lesser Restoration', 2, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Touch', 'V, S', 'Instantaneous', FALSE, FALSE, 'End a condition affecting a creature.', 'OFFICIAL'),
    ('Mirror Image', 2, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', 'Self', 'V, S', '1 minute', FALSE, FALSE, 'Create illusory duplicates of yourself.', 'OFFICIAL'),
    ('Misty Step', 2, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 bonus action', 'Self', 'V', 'Instantaneous', FALSE, FALSE, 'Teleport up to 30 feet.', 'OFFICIAL'),
    ('Scorching Ray', 2, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '120 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'Create three rays of fire.', 'OFFICIAL'),
    ('Shatter', 2, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '60 feet', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'A sudden loud ringing noise.', 'OFFICIAL'),
    ('Spiritual Weapon', 2, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 bonus action', '60 feet', 'V, S', '1 minute', FALSE, FALSE, 'Create a floating, spectral weapon.', 'OFFICIAL'),
    ('Web', 2, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '60 feet', 'V, S, M', '1 hour', TRUE, FALSE, 'Conjure a mass of thick, sticky webbing.', 'OFFICIAL');

-- 3rd Level Spells
INSERT INTO spells (name, level, school_id, casting_time, spell_range, components, duration, concentration, ritual, description, source) VALUES
    ('Counterspell', 3, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 reaction', '60 feet', 'S', 'Instantaneous', FALSE, FALSE, 'Interrupt a creature casting a spell.', 'OFFICIAL'),
    ('Dispel Magic', 3, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', '120 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'End spells on a creature or object.', 'OFFICIAL'),
    ('Fireball', 3, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '150 feet', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'A bright streak flashes to a point and explodes.', 'OFFICIAL'),
    ('Fly', 3, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', 'Touch', 'V, S, M', '10 minutes', TRUE, FALSE, 'Grant a creature flying speed.', 'OFFICIAL'),
    ('Haste', 3, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '30 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Make a creature faster.', 'OFFICIAL'),
    ('Lightning Bolt', 3, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Self (100-foot line)', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'A stroke of lightning.', 'OFFICIAL'),
    ('Revivify', 3, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', 'Touch', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'Return a creature to life.', 'OFFICIAL'),
    ('Spirit Guardians', 3, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', 'Self (15-foot radius)', 'V, S, M', '10 minutes', TRUE, FALSE, 'Call forth spirits to protect you.', 'OFFICIAL');
