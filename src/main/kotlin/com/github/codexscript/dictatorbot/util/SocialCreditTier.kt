package com.github.codexscript.dictatorbot.util

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