# CartMC

What is CartMC? First of all, I do not have a simple answer to that, because the opportunities are limitless, only depends on youre creativity.

CartMC is designed to make shoppings using Minecraft GUIs, divided by unlimited categories that you can create with categories.yml, and with unlimited items per category (that you can create inside categories's folder).

It's not all: CartMC has a cash system based on MySQL that allows you to generate keys and sell it (for real world money) to the player to receive cash inside game, and with this cash, buy every items, kits or everything you want to sell, that him want (I'll show you the commands about this system below).

## STAFF GUIDE

In this section we'll talk about commands for staff usage. Remember that you'll need the permission '**cartmc.admin**'. Please, don't give access to your cash management for strange or untrustable people.

- Use **/cartmc new [quantity of cash]** and you'll receive a key that you can use through **/usekey [the key]** (for player usage) and receive the cash.
- Every key can be deleted simply using **/cartmc del [the key]**.
- If you want to see existing keys and how much she give **(of cash)**, you can simply use **/cartmc keys**. Remember to do not expose these keys because everyone will be able to activate it.
- You can simply give cash to anyone using **/cartmc + [the player name] [the quantity of cash]**.
- Like credit, you can simply take cash of a account using **/cartmc - [player] [quantity]**.
- To change the account cash, use **/cartmc = [player] [new quantity of cash]**.
- Want to see balance of another player? Simply use **/cartmc ? [player name]**.

## PLAYER GUIDE
- Can use **/cash** to see how much him have of cash
- Can use **/cart** (you can configure this command!) to open server shopping

## SETUP GUIDE

### First usage
After run the plugin for first time you'll need to shutdown server and open the created folder (named as "**CartMC**", inside your plugins folder) and go to the "**sql**" section, there you need to place your database credentials to plugin connect to it and manage cash and keys.

You can expect something like that, in **config.yml**:
![alt text](http://image.prntscr.com/image/3d4f9f8467fd440a893a5299b4ebad3a.png "config.yml content")

After you changed your mysql credentials, you'll be able to start the server. Remeber that right now this plugin doesn't supports flatfile, so, if you use wrong mysql credentials or don't configure it, the plugin will not work.

## Things To Do
- Command **/cashtop** to see the list of the most richest players in the server.
- Create support for PostgreSQL
- Create support for flatfile usage (downgrade of performance **:-[**)

## Development API Guide
I'm building this section, soon as possible it will be available for everyone. I'll appreciate your patience. **;-)**

## Donate
My name is **João Pedro Viana**, a brazillian **16 years old boy** that started learning JAVA with **12 years old**, in a **Intel Atom®** with **1GB of RAM**. I'm learning hard to be someone. If you like my work and want to contribute with free softwares, you can donate through of:

**PayPal**: blackthog@gmail.com  
**PagSeguro**: jpedro2014@hotmail.com
