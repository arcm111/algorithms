#!/usr/bin/python

def prod(i, h):
	result = 1
	k = 1
	while k <= i:
		result *= pow(2, pow(2, h - k))
		k += 1
	return result

def prod2(i):
	return pow(2, pow(2, 2 - i))
	
def test(h):
	for i in range(1, h + 1):
		print "prod i = %d: %d" % (i, prod(i, h));
		print "prod2 i = %d: %d" % (i, prod2(i));

test(2)
