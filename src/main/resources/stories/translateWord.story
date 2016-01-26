Scenario: Translate English to Russian word 'car'
Given Start page on Google Translator
When User put word car in textBox
When User click translate button
Then The result of translate will be автомобиль


Scenario: Translate Russian to German word 'человек'
Given Start page on Google Translator
When User change input language to Русский
When User put word человек in textBox
When User change output language to Немецкий
When User click translate button
Then The result of translate will be Mann

