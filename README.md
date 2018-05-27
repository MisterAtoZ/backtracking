#backtrackingalgoritme van het filosofenprobleem

Het algoritme zet de opgegeven personen aan een ronde tafel
en zorgt ervoor dat er geen vijanden van mekaar langseen zitten

werkt voor alle mogelijkheden die ik heb geprobeerd

Als er geen oplossing is werkt het algoritme misschien iets minder mooi
er is een parameter (parameter) die n^2 groot is en als deze op nul is dan is er geen oplossing voor de personen gevonden
en wordt er een lege rij terug gegeven.

Om alleen personen die vrienden zijn langs elkaar te zetten moeten er nog een paar aanpassingen gebeuren:
-de functie vergelijken aanpassen zoals ver2pers
-bij verg2pers de laatste return op false zetten
