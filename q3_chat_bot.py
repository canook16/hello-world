#!/usr/bin/python
import sys
import re
import random
# Josh Novosad 260555158

listOfFiles = sys.argv[1:] # create a list of all potential files
for txtFile in listOfFiles:
	if ".txt" not in txtFile: # check to make sure that all inputs are valid
		print "Not valid text file"
		sys.exit() # if it's not, end the program

list_of_words = [] # initial list of words
for txtFile in listOfFiles: # for each txt file, add to the list of words
	with open(txtFile,"r") as f:
		for line in f: # reads every line
			for word in line.split(): # for each word in the line in the txt file
				if "-" in word: 
					temp = word.split("-")
					for eachWord in temp:
						list_of_words.append(eachWord.lower())
				else:
					list_of_words.append(word.lower())
stopPairs = [] # create list of stop pairs; this is a list of tuples
for i in range(len(list_of_words)): # for each index value in the list of words
	if i+1 < len(list_of_words): # prevents index out of bound error
		if '.' in list_of_words[i]: # check if end markers are in the word
			fixedWord = re.sub("[^a-zA-z]+", "", list_of_words[i])
			tup = (list_of_words[i-1],fixedWord)
			stopPairs.append(tup)
		elif '?' in list_of_words[i]:
			fixedWord = re.sub("[^a-zA-z]+", "", list_of_words[i])
			tup = (list_of_words[i-1],fixedWord)
			stopPairs.append(tup)
		elif '!' in list_of_words[i]:
			fixedWord = re.sub("[^a-zA-z]+", "", list_of_words[i])
			tup = (list_of_words[i-1],fixedWord)
			stopPairs.append(tup)
# at this point, we have a list of tuples of stop pairs, with the second value in each tuple being the final word of a sentence.

for words in list_of_words: # this cleans up the list
	if "-" in words: # this deletes the hyphenated word which was a duplicate
		list_of_words.remove(words)
	if words.isalpha()==False: # if word is not alphabetic, remove the non-alphabetic character and replace it per instruction
		list_index = list_of_words.index(words)
		fixedWord = re.sub("[^a-zA-z]+", "", words)
		list_of_words.remove(words)
		list_of_words.insert(list_index, fixedWord)
# at this point, the list_of_words is up to date, ie. every entry is alphabetic and lowercase

wordPairs = [] # create list of word pair tuples
for i in range(len(list_of_words)): # for each index value in the list of words
	if i+1 < len(list_of_words): # prevents index out of bound error
		tup = (list_of_words[i], list_of_words[i+1]) # create word1 word2 tuple
		wordPairs.append(tup) # put in list
# at this point, we have a list of tuples of word pairs

# Begin the chatbot now that you have the necessary info:
while True: # Continue running unless end conditions are met below
	potentialR1 = []
	wordCount = 0 # initialize (this will be used later to check for 20 words)
	nextWord = None # initialize (this helps for finding the word pairs later on)
	R1exists = False
	inputString = raw_input("Send:\t\t")
	inputWordList = inputString.split(" ") # split words by the space
	lastWordIndex = len(inputWordList) - 1 # need this to access the QN
	lastWord = inputWordList[lastWordIndex].lower()
	if lastWord.isalpha()==False: # if the last word is not alphabetic, remove those characters
		lastWord = re.sub("[^a-zA-z]+", "", lastWord)
	for pairs in wordPairs: # check if the QN exists in the wordpairs list
		if pairs[0]==lastWord: # if it does, put it in a list
			potentialR1.append(pairs[1])
			R1exists = True
	if R1exists is True:
		randomPoint = random.randrange(0,len(potentialR1),1)
		nextWord = potentialR1[randomPoint]
		response = "Receive:\t" + nextWord.capitalize() # formulate response
		wordCount = wordCount + 1
	else: # if R1exists is False, use any word from the text, selected at random
		randomPoint = random.randrange(0,len(wordPairs),1)
		nextWord = wordPairs[randomPoint][0]
		response = "Receive:\t" + nextWord.capitalize() # formulate response
		wordCount = wordCount + 1

	endConditions = False
	while endConditions is False:
		potentialNextWord = []
		noMatch = False
		for pairs in wordPairs: # look for the -RJ for nextWord
			if pairs[0]==nextWord: # if it does, put it in a list
				potentialNextWord.append(pairs[1])
				noMatch = True
		if noMatch is False: # if you didn't find a match, condition (2) is met
			endConditions = True
		else: # in this case, noMatch is true and we find the next word to add
			randomPoint = random.randrange(0,len(potentialNextWord),1)
			nextWord = potentialNextWord[randomPoint]
			response = response + " " + nextWord
			wordCount = wordCount + 1
		for pairs in stopPairs: # check to see if the nextWord leads to a stop pair
			if pairs[1]==nextWord: # if the second word in a stopPair matches the nextWord
				endConditions = True # if found, then condition (1) is met
		if wordCount >= 20: # if the word count reaches 20, condition (3) is met
			endConditions = True
	response = response + "."
	print response
