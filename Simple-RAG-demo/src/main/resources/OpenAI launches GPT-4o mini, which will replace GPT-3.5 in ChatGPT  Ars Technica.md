#### chibi LLM —

## Lower-cost AI language model will be free for ChatGPT users.

![A glowing OpenAI logo on a blue background.](https://cdn.arstechnica.net/wp-content/uploads/2023/10/openai_glowing_blue-800x450.jpg)

Benj Edwards

On Thursday, OpenAI announced the launch of [GPT-4o mini](https://openai.com/index/gpt-4o-mini-advancing-cost-efficient-intelligence/), a new, smaller version of its latest [GPT-4o](https://arstechnica.com/information-technology/2024/05/chatgpt-4o-lets-you-have-real-time-audio-video-conversations-with-emotional-chatbot/) AI language model that will replace [GPT-3.5 Turbo](https://arstechnica.com/information-technology/2023/03/chatgpt-and-whisper-apis-debut-allowing-devs-to-integrate-them-into-apps/) in ChatGPT, reports [CNBC](https://www.cnbc.com/2024/07/18/openai-4o-mini-model-announced.html) and [Bloomberg](https://www.bloomberg.com/news/articles/2024-07-18/openai-releases-gpt-4o-mini-a-cheaper-version-of-flagship-ai-model). It will be available today for free users and those with ChatGPT Plus or Team subscriptions and will come to ChatGPT Enterprise next week.

GPT-4o mini will reportedly be multimodal like its big brother (which [launched in May](https://arstechnica.com/information-technology/2024/05/chatgpt-4o-lets-you-have-real-time-audio-video-conversations-with-emotional-chatbot/)), with image inputs currently enabled in the API. OpenAI says that in the future, GPT-4o mini will be able to interpret images, text, and audio, and also will be able to generate images.

GPT-4o mini supports 128K tokens of input context and a knowledge cutoff of October 2023. It's also very inexpensive as an API product, costing 60 percent less than GPT-3.5 Turbo at 15 cents per million input tokens and 60 cents per million output tokens. Tokens are fragments of data that AI language models use to process information.

Notably, OpenAI says that GPT-4o mini will be the company’s first AI model to use a new technique called "[instruction hierarchy](https://arxiv.org/abs/2404.13208)" that will make an AI model prioritize some instructions over others, which may make it more difficult for people to use to perform [prompt injection attacks](https://arstechnica.com/information-technology/2022/09/twitter-pranksters-derail-gpt-3-bot-with-newly-discovered-prompt-injection-hack/) or [jailbreaks](https://arstechnica.com/information-technology/2023/10/sob-story-about-dead-grandma-tricks-microsoft-ai-into-solving-captcha/), or [system prompt extractions](https://arstechnica.com/information-technology/2023/02/ai-powered-bing-chat-spills-its-secrets-via-prompt-injection-attack/) that subvert built-in fine-tuning or directives given by a system prompt.

Once the model is in the public's hands (GPT-4o mini is currently not available in our instance of ChatGPT), we'll surely see people putting this new protection method to the test.

## Performance

Predictably, OpenAI says that GPT-4o mini performs well on an array of benchmarks like [MMLU](https://paperswithcode.com/sota/multi-task-language-understanding-on-mmlu) (undergraduate level knowledge) and [HumanEval](https://paperswithcode.com/dataset/humaneval) (coding), but the problem is that those benchmarks [don't actually mean much](https://themarkup.org/artificial-intelligence/2024/07/17/everyone-is-judging-ai-by-these-tests-but-experts-say-theyre-close-to-meaningless), and few measure anything useful when it comes to actually using the model in practice. That's because the feel of quality from the output of a model has more to do with style and structure at times than raw factual or mathematical capability. This kind of subjective "vibemarking" is one of the most frustrating things in the AI space right now.

[![A graph by OpenAI shows GPT-4o mini outperforming GPT-4 Turbo on eight cherry-picked benchmarks.](https://cdn.arstechnica.net/wp-content/uploads/2024/07/GPT-4o_mini_benchmarks-640x351.jpg)](https://cdn.arstechnica.net/wp-content/uploads/2024/07/GPT-4o_mini_benchmarks.jpg)

[Enlarge](https://cdn.arstechnica.net/wp-content/uploads/2024/07/GPT-4o_mini_benchmarks.jpg) / A graph by OpenAI shows GPT-4o mini outperforming GPT-4 Turbo on eight cherry-picked benchmarks.

So we'll tell you this: OpenAI says the new model outperformed last year's [GPT-4 Turbo](https://arstechnica.com/information-technology/2023/11/openai-introduces-gpt-4-turbo-larger-memory-lower-cost-new-knowledge/) on the [LMSYS Chatbot Arena](https://arstechnica.com/ai/2023/12/turing-test-on-steroids-chatbot-arena-crowdsources-ratings-for-45-ai-models/) leaderboard, which measures user ratings after having compared the model to another one at random. But even that metric isn't as useful as once hoped in the AI community, [because people have been noticing](https://x.com/VictorTaelin/status/1805331545382654458) that even though mini's big brother (GPT-4o) regularly outperforms GPT-4 Turbo on Chatbot Arena, it tends to produce dramatically less useful outputs in general (they tend to be long-winded, for example, or perform tasks you didn't ask it to do).

## The value of smaller language models

OpenAI isn't the first company to release a smaller version of an existing language model. It's a common practice in the AI industry from vendors such as Meta, Google, and Anthropic. These smaller language models are designed to perform simpler tasks at a lower cost, such as making lists, summarizing, or suggesting words instead of performing deep analysis.

Smaller models are typically aimed at [API users](https://arstechnica.com/information-technology/2023/07/openais-most-powerful-chatbot-api-rolls-out-for-all-paying-customers/), which pay a fixed price per token input and output to use the models in their own applications, but in this case, offering GPT-4o mini for free as part of ChatGPT would ostensibly save money for OpenAI as well.

OpenAI’s head of API product, Olivier Godement, told Bloomberg, "In our mission to enable the bleeding edge, to build the most powerful, useful applications, we of course want to continue doing the frontier models, pushing the envelope here. But we also want to have the best small models out there."

Smaller large language models (LLMs) usually have fewer parameters than larger models. Parameters are numerical stores of value in a neural network that store learned information. Having fewer parameters means an LLM has a smaller neural network, which typically limits the depth of an AI model's ability to make sense of context. Larger-parameter models are typically "deeper thinkers" by virtue of the larger number of connections between concepts stored in those numerical parameters.

However, to complicate things, there isn't always a direct correlation between parameter size and capability. The quality of training data, the efficiency of the model architecture, and the training process itself also impact a model's performance, as we've seen in more capable small models like [Microsoft Phi-3](https://arstechnica.com/information-technology/2024/04/microsofts-phi-3-shows-the-surprising-power-of-small-locally-run-ai-language-models/) recently.

Fewer parameters mean fewer calculations required to run the model, which means either less powerful (and less expensive) GPUs or fewer calculations on existing hardware are necessary, leading to cheaper energy bills and a lower end cost to the user.