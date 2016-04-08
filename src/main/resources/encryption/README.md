#revision
0. The XML-Enc has several security concerns and is broken since Octerber 2011, do not use it alone!
   See https://www.nds.rub.de/media/nds/veroeffentlichungen/2011/10/22/HowToBreakXMLenc.pdf
1. Remove the Type attribute of EncryptedData and add MimeType='text/xml',
   because the entire XML document is encrypted, not a XML element, according to XML-Enc standard.
2. It is meaningless to use a symmetric key(Triple-DES) to encrypt another symmetric key(AES-128),
   therefore, the inner <xenc:EncryptionMethod Algorithm="http://www.w3.org/2001/04/xmlenc#kw-tripledes" />
   is replaced by RSA <xenc:EncryptionMethod Algorithm="http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p" />
3. The previous encypted XML example does not sign the XML document either.
4. The encrypted XML doc has been optimized to reduce redundant namespaces.
5. Both Encryption and Decryption process take 10 to 20 ms,
   based on my desktop running an Intel Core i5-2320 2nd-gen @3.00GHZ