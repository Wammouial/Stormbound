import requests
from bs4 import BeautifulSoup

# Ici exemple pour cartes neutres
url = "https://stormboundkingdomwars.gamepedia.com/Neutral"
output = "cards.txt"

page = requests.get(url)


# Unit : "name" - "name" - mana - deplacement -  player - power - "rarity" - EffectDescription.None - "faction"
# Structure : "name" - "name" - mana - player - power - EffectDescription.None - "faction"
# Spell : "name" - "name" - mana - player - rarity - EffectDescription.None - "faction"

formatStringUnit      = 'cardsMap.put("{}", new Unit("{}", {}, {}, player, {}, "{}", EffectDescription.None, "{}"));'
formatStringStructure = 'cardsMap.put("{}", new Structure("{}", {}, player, {}, EffectDescription.None, "{}"));'
formatStringSpell     = 'cardsMap.put("{}", new Spell("{}", {}, player, "{}", EffectDescription.None));'

bigString = ""

soup = BeautifulSoup(page.text, "lxml")

table = soup.table
lines = table.find_all(style='text-align:left')

for line in lines:
	tds = line.find_all('td')	
	
	if tds[0].text in ("Bladestorm", "Felflares", "Heroic Soldiers", "Emerald Towers",
					   "Execution", "Fort of Ebonrock", "Gifted Recruits", "Joust Champions",
					   "Personal Servers", "Siegebreakers", "Veterans of War", "Warfront Runners"):
		continue
		
	
	cardType = tds[3].text

	if cardType == "Structure":
		
		bigString += formatStringStructure.format(tds[0].text, tds[0].text, tds[6].text, tds[7].text, tds[4].text)
	
	elif cardType == "Spell":
		
		bigString += formatStringSpell.format(tds[0].text, tds[0].text, tds[7].text, tds[4].text)
		
	else:
		
		bigString += formatStringUnit.format(tds[0].text, tds[0].text, tds[6].text, tds[8].text, tds[7].text, tds[5].text, tds[4].text)
		
	if str(tds[1]) != "<td></td>": # Si effet
		
		effect = str(tds[1])[4:-5]
		effect = effect.replace("<b>", "")
		effect = effect.replace("</b>", "")
		
		bigString += " //{}\n".format(effect)
		
	else:
		
		bigString += "\n"
		
with open(output, "w") as writer:
	writer.write(bigString)

