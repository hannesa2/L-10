package com.fsck.k9.message

import com.fsck.k9.Account
import com.fsck.k9.Identity
import com.fsck.k9.helper.ReplyToParser
import com.fsck.k9.mail.buildMessage
import com.google.common.truth.Truth.assertThat
import org.junit.Test

private const val IDENTITY_EMAIL_ADDRESS = "myself@domain.example"

class ReplyActionStrategyTest {
    private val account = createAccount()
    private val replyActionStrategy = ReplyActionStrategy(ReplyToParser())

    @Test
    fun `message sent to only our identity`() {
        val message = buildMessage {
            header("From", "sender@domain.example")
            header("To", IDENTITY_EMAIL_ADDRESS)
        }

        val replyActions = replyActionStrategy.getReplyActions(account, message)

        assertThat(replyActions.defaultAction).isEqualTo(ReplyAction.REPLY)
        assertThat(replyActions.additionalActions).isEmpty()
    }

    @Test
    fun `message sent to our identity and others`() {
        val message = buildMessage {
            header("From", "sender@domain.example")
            header("To", "$IDENTITY_EMAIL_ADDRESS, other@domain.example")
        }

        val replyActions = replyActionStrategy.getReplyActions(account, message)

        assertThat(replyActions.defaultAction).isEqualTo(ReplyAction.REPLY_ALL)
        assertThat(replyActions.additionalActions).containsExactly(ReplyAction.REPLY)
    }

    @Test
    fun `message sent to our identity and others (CC)`() {
        val message = buildMessage {
            header("From", "sender@domain.example")
            header("Cc", "$IDENTITY_EMAIL_ADDRESS, other@domain.example")
        }

        val replyActions = replyActionStrategy.getReplyActions(account, message)

        assertThat(replyActions.defaultAction).isEqualTo(ReplyAction.REPLY_ALL)
        assertThat(replyActions.additionalActions).containsExactly(ReplyAction.REPLY)
    }

    @Test
    fun `message sent to our identity and others (To+CC)`() {
        val message = buildMessage {
            header("From", "sender@domain.example")
            header("To", IDENTITY_EMAIL_ADDRESS)
            header("Cc", "other@domain.example")
        }

        val replyActions = replyActionStrategy.getReplyActions(account, message)

        assertThat(replyActions.defaultAction).isEqualTo(ReplyAction.REPLY_ALL)
        assertThat(replyActions.additionalActions).containsExactly(ReplyAction.REPLY)
    }

    @Test
    fun `message sent to our identity and others (CC+To)`() {
        val message = buildMessage {
            header("From", "sender@domain.example")
            header("To", "other@domain.example")
            header("Cc", IDENTITY_EMAIL_ADDRESS)
        }

        val replyActions = replyActionStrategy.getReplyActions(account, message)

        assertThat(replyActions.defaultAction).isEqualTo(ReplyAction.REPLY_ALL)
        assertThat(replyActions.additionalActions).containsExactly(ReplyAction.REPLY)
    }

    @Test
    fun `message where neither sender nor recipient addresses belong to account`() {
        val message = buildMessage {
            header("From", "sender@domain.example")
            header("To", "recipient@domain.example")
        }

        val replyActions = replyActionStrategy.getReplyActions(account, message)

        assertThat(replyActions.defaultAction).isEqualTo(ReplyAction.REPLY_ALL)
        assertThat(replyActions.additionalActions).containsExactly(ReplyAction.REPLY)
    }

    @Test
    fun `message without any sender or recipient headers`() {
        val message = buildMessage {}

        val replyActions = replyActionStrategy.getReplyActions(account, message)

        assertThat(replyActions.defaultAction).isNull()
        assertThat(replyActions.additionalActions).isEmpty()
    }

    private fun createAccount(): Account {
        return Account("00000000-0000-4000-0000-000000000000").apply {
            identities += Identity(name = "Myself", email = IDENTITY_EMAIL_ADDRESS)
        }
    }
}
