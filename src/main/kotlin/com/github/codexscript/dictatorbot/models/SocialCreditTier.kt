package com.github.codexscript.dictatorbot.models

enum class SocialCreditTier {
    D,
    C,
    B,
    AMINUS {
        override fun toString(): String {
            return "A-"
        }
    },
    A,
    APLUS {
        override fun toString(): String {
            return "A+"
        }
    },
    AA,
    AAA
}