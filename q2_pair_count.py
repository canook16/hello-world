#!/usr/bin/python
import sys
import re
# Josh Novosad 260555158

if len(sys.argv) < 2: # makes sure there is the proper amount of arguments
	print "Not enough arguments"
	sys.exit()

if ".txt" not in sys.argv[1]: # check to make sure it is a text file
	print "Not valid text file"
	sys.exit() # if it's not, end the program

list_of_words = [] # initial list of words; this one will have mistakes such as non-alphabetical characters
		   # also, hyphens are split but the original word is there, but it will be cleared out later on
		   # all words are lowercase if they contain alphabetic characters
with open(sys.argv[1],"r") as f: # read contents of txt file
	for line in f: # reads every line
		for word in line.split(): # for each word in the line in the txt file
			if "-" in word: # if there is a hyphen, split into two words and add to list
				temp = word.split("-")
				for eachWord in temp:
					list_of_words.append(eachWord.lower())
			else:
				list_of_words.append(word.lower()) # add the lowercase word to the list

for words in list_of_words: # this cleans up the list
	if "-" in words: # this deletes the hyphenated word which was a duplicate
		list_of_words.remove(words)
	if words.isalpha()==False: # if word is not alphabetic, remove the non-alphabetic character and replace it per instruction
		list_index = list_of_words.index(words)
		fixedWord = re.sub("[^a-zA-z]+", "", words)
		list_of_words.remove(words)
		list_of_words.insert(list_index, fixedWord)
# at this point, the list_of_words is up to date, ie. every entry is alphabetic and lowercase

temp = "-"
wordPairs = [] # create list of word pairs (empty at this point)
for i in range(len(list_of_words)): # for each index value in the list of words
	if i+1 < len(list_of_words): # prevents index out of bound error
		c = list_of_words[i] + temp + list_of_words[i+1] # create word1-word2 format
		wordPairs.append(c) # put in list

# at this point, we have a list of word pairs, all in lowercase, and all alphabetic
pairDictionary = dict()
for pair in wordPairs:
	if pair not in pairDictionary:
		pairDictionary[pair] = 1
	else:
		pairDictionary[pair] += 1
sortedList = [] # create a sorted list
for pairFrequencies in pairDictionary:
	k = (pairFrequencies,pairDictionary[pairFrequencies])
	sortedList.append(k)
sortedList = sorted(sortedList, key=lambda x: x[1], reverse=True)
for pair in sortedList:
	print "%s:%d" % (pair[0],pair[1])
