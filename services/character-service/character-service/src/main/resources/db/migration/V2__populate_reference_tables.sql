-- =============================================
-- Populate D&D 5E Reference Tables
-- Note: alignments, species, character_classes, backgrounds
-- are managed by compendium-service
-- =============================================

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
INSERT INTO spells (name, level, school_id, casting_time, spell_range, components, duration, concentration, ritual, description) VALUES
    ('Acid Splash', 0, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '60 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'You hurl a bubble of acid.'),
    ('Blade Ward', 0, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Self', 'V, S', '1 round', FALSE, FALSE, 'Resistance to bludgeoning, piercing, and slashing damage.'),
    ('Chill Touch', 0, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', '120 feet', 'V, S', '1 round', FALSE, FALSE, 'A ghostly, skeletal hand assails your foe.'),
    ('Dancing Lights', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '120 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Create up to four torch-sized lights.'),
    ('Druidcraft', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '30 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'Whispering to the spirits of nature.'),
    ('Eldritch Blast', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '120 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'A beam of crackling energy streaks toward a creature.'),
    ('Fire Bolt', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '120 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'You hurl a mote of fire at a creature or object.'),
    ('Friends', 0, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', 'Self', 'S, M', '1 minute', TRUE, FALSE, 'Gain advantage on Charisma checks.'),
    ('Guidance', 0, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', 'Touch', 'V, S', '1 minute', TRUE, FALSE, 'Add 1d4 to one ability check.'),
    ('Light', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Touch', 'V, M', '1 hour', FALSE, FALSE, 'Object sheds bright light in a 20-foot radius.'),
    ('Mage Hand', 0, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '30 feet', 'V, S', '1 minute', FALSE, FALSE, 'A spectral, floating hand appears.'),
    ('Mending', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 minute', 'Touch', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'Repair a single break or tear in an object.'),
    ('Message', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '120 feet', 'V, S, M', '1 round', FALSE, FALSE, 'Whisper a message to a creature.'),
    ('Minor Illusion', 0, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', '30 feet', 'S, M', '1 minute', FALSE, FALSE, 'Create a sound or an image of an object.'),
    ('Poison Spray', 0, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '10 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'Project a puff of noxious gas.'),
    ('Prestidigitation', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '10 feet', 'V, S', '1 hour', FALSE, FALSE, 'Minor magical tricks.'),
    ('Produce Flame', 0, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', 'Self', 'V, S', '10 minutes', FALSE, FALSE, 'A flickering flame appears in your hand.'),
    ('Ray of Frost', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '60 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'A frigid beam of blue-white light.'),
    ('Resistance', 0, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Touch', 'V, S, M', '1 minute', TRUE, FALSE, 'Add 1d4 to one saving throw.'),
    ('Sacred Flame', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '60 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'Flame-like radiance descends on a creature.'),
    ('Shillelagh', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 bonus action', 'Touch', 'V, S, M', '1 minute', FALSE, FALSE, 'Imbue a club or quarterstaff with nature''s power.'),
    ('Shocking Grasp', 0, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Touch', 'V, S', 'Instantaneous', FALSE, FALSE, 'Lightning springs from your hand.'),
    ('Spare the Dying', 0, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', 'Touch', 'V, S', 'Instantaneous', FALSE, FALSE, 'Stabilize a dying creature.'),
    ('Thaumaturgy', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '30 feet', 'V', '1 minute', FALSE, FALSE, 'Manifest a minor wonder.'),
    ('Thorn Whip', 0, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '30 feet', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'A vine-like whip covered in thorns.'),
    ('True Strike', 0, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', '30 feet', 'S', '1 round', TRUE, FALSE, 'Gain insight into target''s defenses.'),
    ('Vicious Mockery', 0, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '60 feet', 'V', 'Instantaneous', FALSE, FALSE, 'Unleash a string of insults laced with subtle enchantments.');

-- 1st Level Spells
INSERT INTO spells (name, level, school_id, casting_time, spell_range, components, duration, concentration, ritual, description) VALUES
    ('Alarm', 1, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 minute', '30 feet', 'V, S, M', '8 hours', FALSE, TRUE, 'Set an alarm against unwanted intrusion.'),
    ('Animal Friendship', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '30 feet', 'V, S, M', '24 hours', FALSE, FALSE, 'Convince a beast that you mean it no harm.'),
    ('Armor of Agathys', 1, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Self', 'V, S, M', '1 hour', FALSE, FALSE, 'Gain temporary hit points and cold damage aura.'),
    ('Arms of Hadar', 1, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', 'Self (10-foot radius)', 'V, S', 'Instantaneous', FALSE, FALSE, 'Tendrils of dark energy erupt from you.'),
    ('Bane', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '30 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Up to three creatures subtract 1d4 from attacks and saves.'),
    ('Bless', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '30 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Bless up to three creatures with 1d4 bonus.'),
    ('Burning Hands', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Self (15-foot cone)', 'V, S', 'Instantaneous', FALSE, FALSE, 'A thin sheet of flames shoots forth from your fingertips.'),
    ('Charm Person', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '30 feet', 'V, S', '1 hour', FALSE, FALSE, 'Attempt to charm a humanoid.'),
    ('Chromatic Orb', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '90 feet', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'Hurl an orb of energy at a creature.'),
    ('Color Spray', 1, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', 'Self (15-foot cone)', 'V, S, M', '1 round', FALSE, FALSE, 'A dazzling array of flashing, colored light.'),
    ('Command', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '60 feet', 'V', '1 round', FALSE, FALSE, 'Speak a one-word command to a creature.'),
    ('Comprehend Languages', 1, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', 'Self', 'V, S, M', '1 hour', FALSE, TRUE, 'Understand any spoken language.'),
    ('Create or Destroy Water', 1, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '30 feet', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'Create or destroy up to 10 gallons of water.'),
    ('Cure Wounds', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Touch', 'V, S', 'Instantaneous', FALSE, FALSE, 'A creature you touch regains hit points.'),
    ('Detect Evil and Good', 1, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', 'Self', 'V, S', '10 minutes', TRUE, FALSE, 'Sense the presence of otherworldly creatures.'),
    ('Detect Magic', 1, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', 'Self', 'V, S', '10 minutes', TRUE, TRUE, 'Sense the presence of magic.'),
    ('Detect Poison and Disease', 1, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', 'Self', 'V, S, M', '10 minutes', TRUE, TRUE, 'Sense the presence and location of poisons and diseases.'),
    ('Disguise Self', 1, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', 'Self', 'V, S', '1 hour', FALSE, FALSE, 'Change your appearance.'),
    ('Dissonant Whispers', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '60 feet', 'V', 'Instantaneous', FALSE, FALSE, 'Whisper a discordant melody.'),
    ('Divine Favor', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 bonus action', 'Self', 'V, S', '1 minute', TRUE, FALSE, 'Your weapon attacks deal extra radiant damage.'),
    ('Entangle', 1, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '90 feet', 'V, S', '1 minute', TRUE, FALSE, 'Grasping weeds and vines sprout from the ground.'),
    ('Expeditious Retreat', 1, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 bonus action', 'Self', 'V, S', '10 minutes', TRUE, FALSE, 'Take the Dash action as a bonus action.'),
    ('Faerie Fire', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '60 feet', 'V', '1 minute', TRUE, FALSE, 'Objects and creatures are outlined in light.'),
    ('False Life', 1, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', 'Self', 'V, S, M', '1 hour', FALSE, FALSE, 'Bolster yourself with a necromantic facsimile of life.'),
    ('Feather Fall', 1, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 reaction', '60 feet', 'V, M', '1 minute', FALSE, FALSE, 'Slow the rate of descent for falling creatures.'),
    ('Find Familiar', 1, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 hour', '10 feet', 'V, S, M', 'Instantaneous', FALSE, TRUE, 'Gain the service of a familiar.'),
    ('Fog Cloud', 1, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '120 feet', 'V, S', '1 hour', TRUE, FALSE, 'Create a sphere of fog.'),
    ('Goodberry', 1, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', 'Touch', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'Create up to ten berries infused with magic.'),
    ('Grease', 1, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '60 feet', 'V, S, M', '1 minute', FALSE, FALSE, 'Cover the ground in slippery grease.'),
    ('Guiding Bolt', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '120 feet', 'V, S', '1 round', FALSE, FALSE, 'A flash of light streaks toward a creature.'),
    ('Healing Word', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 bonus action', '60 feet', 'V', 'Instantaneous', FALSE, FALSE, 'A creature regains hit points.'),
    ('Hellish Rebuke', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 reaction', '60 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'You surround yourself with hellish flames.'),
    ('Heroism', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', 'Touch', 'V, S', '1 minute', TRUE, FALSE, 'A creature is imbued with bravery.'),
    ('Hex', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 bonus action', '90 feet', 'V, S, M', '1 hour', TRUE, FALSE, 'Place a curse on a creature.'),
    ('Hunter''s Mark', 1, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 bonus action', '90 feet', 'V', '1 hour', TRUE, FALSE, 'Mark a creature as your quarry.'),
    ('Identify', 1, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 minute', 'Touch', 'V, S, M', 'Instantaneous', FALSE, TRUE, 'Learn the properties of a magic item.'),
    ('Illusory Script', 1, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 minute', 'Touch', 'S, M', '10 days', FALSE, TRUE, 'Write a hidden message.'),
    ('Inflict Wounds', 1, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', 'Touch', 'V, S', 'Instantaneous', FALSE, FALSE, 'Make a melee spell attack to deal necrotic damage.'),
    ('Jump', 1, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', 'Touch', 'V, S, M', '1 minute', FALSE, FALSE, 'Triple a creature''s jump distance.'),
    ('Longstrider', 1, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', 'Touch', 'V, S, M', '1 hour', FALSE, FALSE, 'Increase a creature''s speed by 10 feet.'),
    ('Mage Armor', 1, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Touch', 'V, S, M', '8 hours', FALSE, FALSE, 'Surround a creature with a magical force.'),
    ('Magic Missile', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '120 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'Create three glowing darts of magical force.'),
    ('Protection from Evil and Good', 1, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Touch', 'V, S, M', '10 minutes', TRUE, FALSE, 'Protect a creature against certain types of creatures.'),
    ('Purify Food and Drink', 1, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '10 feet', 'V, S', 'Instantaneous', FALSE, TRUE, 'Render food and drink free of poison and disease.'),
    ('Ray of Sickness', 1, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', '60 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'A ray of sickening greenish energy.'),
    ('Sanctuary', 1, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 bonus action', '30 feet', 'V, S, M', '1 minute', FALSE, FALSE, 'Ward a creature against attack.'),
    ('Shield', 1, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 reaction', 'Self', 'V, S', '1 round', FALSE, FALSE, 'An invisible barrier of magical force.'),
    ('Shield of Faith', 1, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 bonus action', '60 feet', 'V, S, M', '10 minutes', TRUE, FALSE, 'A shimmering field appears around a creature.'),
    ('Silent Image', 1, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', '60 feet', 'V, S, M', '10 minutes', TRUE, FALSE, 'Create the image of an object, creature, or phenomenon.'),
    ('Sleep', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '90 feet', 'V, S, M', '1 minute', FALSE, FALSE, 'Send creatures into a magical slumber.'),
    ('Speak with Animals', 1, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', 'Self', 'V, S', '10 minutes', FALSE, TRUE, 'Gain the ability to comprehend and communicate with beasts.'),
    ('Tasha''s Hideous Laughter', 1, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '30 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'A creature falls prone and is incapacitated.'),
    ('Tensers Floating Disk', 1, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '30 feet', 'V, S, M', '1 hour', FALSE, TRUE, 'Create a floating disk of force.'),
    ('Thunderwave', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Self (15-foot cube)', 'V, S', 'Instantaneous', FALSE, FALSE, 'A wave of thunderous force sweeps out from you.'),
    ('Unseen Servant', 1, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '60 feet', 'V, S, M', '1 hour', FALSE, TRUE, 'Create an invisible, mindless, shapeless force.'),
    ('Witch Bolt', 1, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '30 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'A beam of crackling, blue energy.');

-- 2nd Level Spells (selection of common spells)
INSERT INTO spells (name, level, school_id, casting_time, spell_range, components, duration, concentration, ritual, description) VALUES
    ('Aid', 2, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', '30 feet', 'V, S, M', '8 hours', FALSE, FALSE, 'Bolster allies with toughness and resolve.'),
    ('Alter Self', 2, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', 'Self', 'V, S', '1 hour', TRUE, FALSE, 'Assume a different form.'),
    ('Arcane Lock', 2, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Touch', 'V, S, M', 'Until dispelled', FALSE, FALSE, 'Magically lock a door, window, or container.'),
    ('Blindness/Deafness', 2, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', '30 feet', 'V', '1 minute', FALSE, FALSE, 'Blind or deafen a foe.'),
    ('Blur', 2, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', 'Self', 'V', '1 minute', TRUE, FALSE, 'Your body becomes blurred.'),
    ('Calm Emotions', 2, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '60 feet', 'V, S', '1 minute', TRUE, FALSE, 'Attempt to suppress strong emotions.'),
    ('Continual Flame', 2, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Touch', 'V, S, M', 'Until dispelled', FALSE, FALSE, 'A flame springs forth from an object.'),
    ('Darkness', 2, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '60 feet', 'V, M', '10 minutes', TRUE, FALSE, 'Magical darkness spreads from a point.'),
    ('Darkvision', 2, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', 'Touch', 'V, S, M', '8 hours', FALSE, FALSE, 'Grant a creature darkvision.'),
    ('Detect Thoughts', 2, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', 'Self', 'V, S, M', '1 minute', TRUE, FALSE, 'Read the thoughts of certain creatures.'),
    ('Enhance Ability', 2, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', 'Touch', 'V, S, M', '1 hour', TRUE, FALSE, 'Bestow a magical enhancement on a creature.'),
    ('Enlarge/Reduce', 2, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '30 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Change a creature''s size.'),
    ('Find Steed', 2, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '10 minutes', '30 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'Summon a spirit that assumes the form of a mount.'),
    ('Flame Blade', 2, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 bonus action', 'Self', 'V, S, M', '10 minutes', TRUE, FALSE, 'Evoke a fiery blade in your hand.'),
    ('Flaming Sphere', 2, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '60 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Create a sphere of fire.'),
    ('Gentle Repose', 2, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', 'Touch', 'V, S, M', '10 days', FALSE, TRUE, 'Protect a corpse from decay.'),
    ('Heat Metal', 2, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '60 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Cause metal to glow red-hot.'),
    ('Hold Person', 2, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '60 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Paralyze a humanoid.'),
    ('Invisibility', 2, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', 'Touch', 'V, S, M', '1 hour', TRUE, FALSE, 'A creature becomes invisible.'),
    ('Knock', 2, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '60 feet', 'V', 'Instantaneous', FALSE, FALSE, 'Unlock a door, chest, or container.'),
    ('Lesser Restoration', 2, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Touch', 'V, S', 'Instantaneous', FALSE, FALSE, 'End a condition affecting a creature.'),
    ('Levitate', 2, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '60 feet', 'V, S, M', '10 minutes', TRUE, FALSE, 'Cause a creature or object to rise into the air.'),
    ('Locate Object', 2, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', 'Self', 'V, S, M', '10 minutes', TRUE, FALSE, 'Sense the direction to a familiar object.'),
    ('Magic Weapon', 2, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 bonus action', 'Touch', 'V, S', '1 hour', TRUE, FALSE, 'Make a weapon magical.'),
    ('Mirror Image', 2, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', 'Self', 'V, S', '1 minute', FALSE, FALSE, 'Create illusory duplicates of yourself.'),
    ('Misty Step', 2, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 bonus action', 'Self', 'V', 'Instantaneous', FALSE, FALSE, 'Teleport up to 30 feet.'),
    ('Moonbeam', 2, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '120 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'A silvery beam of pale light shines down.'),
    ('Pass without Trace', 2, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Self', 'V, S, M', '1 hour', TRUE, FALSE, 'A veil of shadows and silence.'),
    ('Prayer of Healing', 2, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '10 minutes', '30 feet', 'V', 'Instantaneous', FALSE, FALSE, 'Up to six creatures regain hit points.'),
    ('Protection from Poison', 2, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Touch', 'V, S', '1 hour', FALSE, FALSE, 'Neutralize poison affecting a creature.'),
    ('Scorching Ray', 2, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '120 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'Create three rays of fire.'),
    ('See Invisibility', 2, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', 'Self', 'V, S, M', '1 hour', FALSE, FALSE, 'See invisible creatures and objects.'),
    ('Shatter', 2, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '60 feet', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'A sudden loud ringing noise.'),
    ('Silence', 2, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', '120 feet', 'V, S', '10 minutes', TRUE, TRUE, 'Create a zone of silence.'),
    ('Spider Climb', 2, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', 'Touch', 'V, S, M', '1 hour', TRUE, FALSE, 'Gain the ability to climb on walls and ceilings.'),
    ('Spiritual Weapon', 2, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 bonus action', '60 feet', 'V, S', '1 minute', FALSE, FALSE, 'Create a floating, spectral weapon.'),
    ('Suggestion', 2, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '30 feet', 'V, M', '8 hours', TRUE, FALSE, 'Suggest a course of activity.'),
    ('Web', 2, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '60 feet', 'V, S, M', '1 hour', TRUE, FALSE, 'Conjure a mass of thick, sticky webbing.'),
    ('Zone of Truth', 2, (SELECT id FROM spell_schools WHERE name = 'Enchantment'), '1 action', '60 feet', 'V, S', '10 minutes', FALSE, FALSE, 'Create a zone where creatures cannot lie.');

-- 3rd Level Spells (selection of common spells)
INSERT INTO spells (name, level, school_id, casting_time, spell_range, components, duration, concentration, ritual, description) VALUES
    ('Animate Dead', 3, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 minute', '10 feet', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'Create undead servants.'),
    ('Beacon of Hope', 3, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', '30 feet', 'V, S', '1 minute', TRUE, FALSE, 'Allies gain advantage on Wisdom saves and healing.'),
    ('Bestow Curse', 3, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', 'Touch', 'V, S', '1 minute', TRUE, FALSE, 'Place a curse on a creature.'),
    ('Blink', 3, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', 'Self', 'V, S', '1 minute', FALSE, FALSE, 'Vanish and reappear on the Ethereal Plane.'),
    ('Call Lightning', 3, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '120 feet', 'V, S', '10 minutes', TRUE, FALSE, 'Call down bolts of lightning.'),
    ('Clairvoyance', 3, (SELECT id FROM spell_schools WHERE name = 'Divination'), '10 minutes', '1 mile', 'V, S, M', '10 minutes', TRUE, FALSE, 'Create an invisible sensor.'),
    ('Conjure Animals', 3, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '60 feet', 'V, S', '1 hour', TRUE, FALSE, 'Summon fey spirits in animal forms.'),
    ('Counterspell', 3, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 reaction', '60 feet', 'S', 'Instantaneous', FALSE, FALSE, 'Interrupt a creature casting a spell.'),
    ('Create Food and Water', 3, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '30 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'Create food and water.'),
    ('Daylight', 3, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '60 feet', 'V, S', '1 hour', FALSE, FALSE, 'Create a sphere of bright light.'),
    ('Dispel Magic', 3, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', '120 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'End spells on a creature or object.'),
    ('Fear', 3, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', 'Self (30-foot cone)', 'V, S, M', '1 minute', TRUE, FALSE, 'Project an image of a creature''s worst fears.'),
    ('Fireball', 3, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', '150 feet', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'A bright streak flashes to a point and explodes.'),
    ('Fly', 3, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', 'Touch', 'V, S, M', '10 minutes', TRUE, FALSE, 'Grant a creature flying speed.'),
    ('Gaseous Form', 3, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', 'Touch', 'V, S, M', '1 hour', TRUE, FALSE, 'Transform into a misty cloud.'),
    ('Glyph of Warding', 3, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 hour', 'Touch', 'V, S, M', 'Until dispelled', FALSE, FALSE, 'Inscribe a glyph that triggers a spell.'),
    ('Haste', 3, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '30 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Make a creature faster.'),
    ('Hypnotic Pattern', 3, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', '120 feet', 'S, M', '1 minute', TRUE, FALSE, 'Create a twisting pattern of colors.'),
    ('Leomund''s Tiny Hut', 3, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 minute', 'Self (10-foot hemisphere)', 'V, S, M', '8 hours', FALSE, TRUE, 'Create a dome of force.'),
    ('Lightning Bolt', 3, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Self (100-foot line)', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'A stroke of lightning.'),
    ('Magic Circle', 3, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 minute', '10 feet', 'V, S, M', '1 hour', FALSE, FALSE, 'Create a cylinder of magical energy.'),
    ('Major Image', 3, (SELECT id FROM spell_schools WHERE name = 'Illusion'), '1 action', '120 feet', 'V, S, M', '10 minutes', TRUE, FALSE, 'Create a complex illusion.'),
    ('Mass Healing Word', 3, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 bonus action', '60 feet', 'V', 'Instantaneous', FALSE, FALSE, 'Up to six creatures regain hit points.'),
    ('Nondetection', 3, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Touch', 'V, S, M', '8 hours', FALSE, FALSE, 'Hide a creature from divination magic.'),
    ('Plant Growth', 3, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action or 8 hours', '150 feet', 'V, S', 'Instantaneous', FALSE, FALSE, 'Cause plants to grow.'),
    ('Protection from Energy', 3, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Touch', 'V, S', '1 hour', TRUE, FALSE, 'Grant resistance to one damage type.'),
    ('Remove Curse', 3, (SELECT id FROM spell_schools WHERE name = 'Abjuration'), '1 action', 'Touch', 'V, S', 'Instantaneous', FALSE, FALSE, 'End curses on a creature or object.'),
    ('Revivify', 3, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', 'Touch', 'V, S, M', 'Instantaneous', FALSE, FALSE, 'Return a creature to life.'),
    ('Sending', 3, (SELECT id FROM spell_schools WHERE name = 'Evocation'), '1 action', 'Unlimited', 'V, S, M', '1 round', FALSE, FALSE, 'Send a short message to a creature.'),
    ('Slow', 3, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '120 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Slow up to six creatures.'),
    ('Speak with Dead', 3, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', '10 feet', 'V, S, M', '10 minutes', FALSE, FALSE, 'Ask a corpse questions.'),
    ('Spirit Guardians', 3, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', 'Self (15-foot radius)', 'V, S, M', '10 minutes', TRUE, FALSE, 'Call forth spirits to protect you.'),
    ('Stinking Cloud', 3, (SELECT id FROM spell_schools WHERE name = 'Conjuration'), '1 action', '90 feet', 'V, S, M', '1 minute', TRUE, FALSE, 'Create a sphere of nauseating gas.'),
    ('Tongues', 3, (SELECT id FROM spell_schools WHERE name = 'Divination'), '1 action', 'Touch', 'V, M', '1 hour', FALSE, FALSE, 'Grant the ability to understand any language.'),
    ('Vampiric Touch', 3, (SELECT id FROM spell_schools WHERE name = 'Necromancy'), '1 action', 'Self', 'V, S', '1 minute', TRUE, FALSE, 'Deal necrotic damage and heal yourself.'),
    ('Water Breathing', 3, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '30 feet', 'V, S, M', '24 hours', FALSE, TRUE, 'Grant the ability to breathe underwater.'),
    ('Water Walk', 3, (SELECT id FROM spell_schools WHERE name = 'Transmutation'), '1 action', '30 feet', 'V, S, M', '1 hour', FALSE, TRUE, 'Grant the ability to walk on water.');
