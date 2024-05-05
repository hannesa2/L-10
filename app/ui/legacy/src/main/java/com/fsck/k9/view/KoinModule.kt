package com.fsck.k9.view

import com.fsck.k9.helper.ReplyToParser
import com.fsck.k9.message.ReplyActionStrategy
import com.fsck.k9.ui.helper.RelativeDateTimeFormatter
import org.koin.dsl.module

val viewModule = module {
    single { WebViewConfigProvider(themeManager = get()) }
    factory { RelativeDateTimeFormatter(context = get(), clock = get()) }
    factory { ReplyToParser() }
    factory { ReplyActionStrategy(replyRoParser = get()) }
}
