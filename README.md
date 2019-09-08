# textToPicHidder
Hide an encrypted message into a picture

This little app allow to hide a message into a picture
To hide text message in a picture :
-Select a valid picture
-Enter a 32 characters key, save it carefully and share it with the people you want to share your message with
-Type a message in the textArea
-Click on the "Crypt" Button

If the checkbox "key" is not ticked, then an internal AES 256 key will be used to crypt your message, bu I highly recommend
you to use your own key for security reasons.

To decrypt text in picture :
-Select a valid picture to decrypt
-Enter the 32 character key the message has been crypted with
-Click the "Decrypt" button

And the decrypted text will appear in the textArea
------------------------------
Detailed description of the app

When you click the crypt button, your text is crypted in AES 256 thanks to the key you provided
The encrypted information is converted in binary and injected in the last bit of each composant RGB of the pixel 1, 2, ...
and so on until all the encrypted information is stored in the picture.
Obviously there is a limit of size of message that can be stored : the more the picture has pixels, the more you can hide info
into it.

For decryption it's just the same process but reversed.

The same key is used for encryption and decryption since AES is a symmetric encryption algorithm.
That's why it's recommended to let the "key" checkbox ticked and use your own key, because if someone managed to
find out the default key used by the programm (via retro engineering this programm for example), he would be able to
decrypt your message.


Some improvement have yet to be done such:
-precise the number of character remaining (before the picture cant store any further information because its full)
-generate jpg, bmp,... instead of only "png" (the problem is when saved in format such as jpg, the message is lost because of the compression algorithm)
-AES 256 is still supposed to be secure, but for how long again? Use a strongger AES key may be necessary in the future
