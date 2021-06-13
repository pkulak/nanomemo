Nano Memo
=========

A super-simple, off-chain storage for [Nano](https://nano.org/) memo lines
(and possibly other metadata in the future). There are existing APIs for this,
but it's so simple, I figured I'd whip up an open-source solution.

Huge shout out to the wonderful [JNano](https://github.com/koczadly/jNano)
project, which took this project from a reasonable amount of work down to
almost nothing.

Create a Memo
--------

You create a memo by passing most of the block to the API. This is so you
can create the memo concurrent with the block on the network (or even before
you create the network block). Since the API is write-once, this guarantees that
you own the memo for your block, as block IDs can't be known ahead of time.

A request is authorized by signing the payload with your private key (the public
key being the `account` propery), using the exact signing method of Nano itself
(ED25519 + Blake2b). This can be tricky to get right with command line tools, so
for this example we are piping a file directly into Httpie, for which we computed
the signature beforehand.

**Body**

    {
        "account": "nano_1pu7p5n3ghq1i1p4rhmek41f5add1uh34xpb94nkbxe8g4a6x1p69emk8y1d",
        "block": "5855E3B99E1E1DE391B9E24A2E05010B604E1F26F4ABEC194F7A589B3C62F80C",
        "link": "nano_1jwc9njxqe4dhth8au79ifxij1x5k5hwmn6eoprat47yfoataxqjbgsjrery",
        "memo": "Thanks for the pizza!"
    }

**Request**

    cat body | http POST https://memo.pkulak.com/blocks \
        Authorization:"Signature 65CA7D8580D6278F36B20A9DCD97AE9DD8C846C3A72C9B0BAFB6396B59117F1F63EB103FAC11EA6CE17685A4A1F6ED7C60E21AAF083B08AC0D61C6603AD3780A"

**Response**

    HTTP/1.1 200 OK

Retrieve a Memo
---------------

To get a memo, authorization is the same (in this case the public key is the `link`
property), but you sign the HTTP path. You also need to send the account you're
expecting to have received from, as a verification.

**Request**

    http https://memo.pkulak.com/blocks/5855E3B99E1E1DE391B9E24A2E05010B604E1F26F4ABEC194F7A589B3C62F80C/memo \
        Authorization:"Signature A489C20E9B3D1CC38A0F26DB05583C873B826B40F522FDD986664FAC7B8DDAFF1E3410C487DEA4B38344F717AB6DB0C97C83C7022EC45A1BC7A22BD35D298F0A" \
        account==nano_1pu7p5n3ghq1i1p4rhmek41f5add1uh34xpb94nkbxe8g4a6x1p69emk8y1d

**Response**

    HTTP/1.1 200 OK
    Content-Length: 21
    Content-Type: text/plain; charset=UTF-8

    Thanks for the pizza!

<hr>
<sup>
Donations welcome!

[nano_3ytxuddbpkspahrxn6s5o67bszuc3u3ye97xdmpgsg7p9ymw96yogacjsznp](https://tools.nanos.cc/?tool=pay&address=nano_3ytxuddbpkspahrxn6s5o67bszuc3u3ye97xdmpgsg7p9ymw96yogacjsznp&amount=&recipient=Phil&message=Donation%20to%20Phil)
</sup>
