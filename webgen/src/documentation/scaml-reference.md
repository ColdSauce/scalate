# Scaml (Scala Markup Language)

* Table of contents
{:toc}

Scaml is a markup language
that's used to cleanly and simply describe the XHTML of any web document,
without the use of inline code.
Scaml functions as a replacement
for inline page templating systems such as PHP, ERB, and ASP.
However, Scaml avoids the need for explicitly coding XHTML into the template,
because it is actually an abstract description of the XHTML,
with some code to generate dynamic content.

## Features

* Whitespace active
* Well-formatted markup
* DRY
* Follows CSS conventions
* Integrates Scala code
* Implements templates with the .scaml extension

## Plain Text

A substantial portion of any HTML document is its content,
which is plain old text.
Any Scaml line that's not interpreted as something else
is taken to be plain text, and passed through unmodified.
For example:

    %gee
      %whiz
        Wow this is cool!

is compiled to:

    <gee>
      <whiz>
        Wow this is cool!
      </whiz>
    </gee>

Note that HTML tags are passed through unmodified as well.
If you have some HTML you don't want to convert to Scaml,
or you're converting a file line-by-line,
you can just include it as-is.
For example:

    %p
      <div id="blah">Blah!</div>

is compiled to:

    <p>
      <div id="blah">Blah!</div>
    </p>

### Escaping: `\`

The backslash character escapes the first character of a line,
allowing use of otherwise interpreted characters as plain text.
For example:

    %title
      = title
      \= title

is compiled to:

    <title>
      MyPage
      = title
    </title>

## HTML Elements


### Element Name: `%`

The percent character is placed at the beginning of a line.
It's followed immediately by the name of an element,
then optionally by modifiers (see below), a space,
and text to be rendered inside the element.
It creates an element in the form of `<element></element>`.
For example:

    %one
      %two
        %three Hey there

is compiled to:

    <one>
      <two>
        <three>Hey there</three>
      </two>
    </one>

Any string is a valid element name;
Scaml will automatically generate opening and closing tags for any element.

### Attributes: `{}` or `()` {#attributes}

TODO: only simple expressions are supported in the following section.. need to expand to
support complex scala expressions too.

Brackets represent a Scala Map
that is used for specifying the attributes of an element.
Ruby hash syntax is used instead of Scala syntax to 
preserve a higher level of compatibility with the original
Haml implementation.
It is translated and evaluated as a Scala Map,
so logic will work in it and local variables may be used.
Quote characters within the attribute
will be replaced by appropriate escape sequences.
The hash is placed after the tag is defined.
For example:

    %html{:xmlns => "http://www.w3.org/1999/xhtml", "xml:lang" => "en", :lang => "en"}

is compiled to:

    <html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en' lang='en'></html>

Attribute hashes can also be stretched out over multiple lines
to accommodate many attributes.
However, newlines may only be placed immediately after commas.
For example:

    %script{:type => "text/javascript",
            :src  => "javascripts/script"}

is compiled to:

    <script type='text/javascript' src='javascripts/script'></script>
    

#### HTML-style Attributes: `()`

Scaml also supports a terser, less Scala-specific attribute syntax
based on HTML's attributes.
These are used with parentheses instead of brackets, like so:

    %html(xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en")

<!-->
Scala variables can be used by omitting the quotes.
For example:

    %a(title=title href=href) Stuff

This is the same as:

    %a{:title =>title, :href => href} Stuff

Because there are no commas separating attributes, though,
more complicated expressions aren't allowed.
For those you'll have to use the `{}` syntax.
-->

You can use both syntaxes together:

    %a(title="Hello"){:href => "http://scalate.fusesource.org"} Stuff

<!-- TODO:
You can also use `#{}` interpolation to insert complicated expressions
in a HTML-style attribute:

    %span(class="widget_#{widget.number}")
-->

HTML-style attributes can be stretched across multiple lines
just like hash-style attributes:

    %script(type="text/javascript"
            src="javascripts/script")

<!-- TODO
#### Attribute Methods

A Scala method call that returns a hash
can be substituted for the hash contents.
For example, {Scaml::Helpers} defines the following method:

    def html_attrs(lang = 'en-US')
      {:xmlns => "http://www.w3.org/1999/xhtml", 'xml:lang' => lang, :lang => lang}
    end

This can then be used in Scaml, like so:

    %html{html_attrs('fr-fr')}

This is compiled to:

    <html lang='fr-fr' xml:lang='fr-fr' xmlns='http://www.w3.org/1999/xhtml'>
    </html>

You can use as many such attribute methods as you want
by separating them with commas,
like a Scala argument list.
All the hashes will me merged together, from left to right.
For example, if you defined

    def hash1
      {:bread => 'white', :filling => 'peanut butter and jelly'}
    end

    def hash2
      {:bread => 'whole wheat'}
    end

then

    %sandwich{hash1, hash2, :delicious => true}/

would compile to:

    <sandwich bread='whole wheat' delicious='true' filling='peanut butter and jelly' />

Note that the Scaml attributes list has the same syntax as a Scala method call.
This means that any attribute methods must come before the hash literal.

Attribute methods aren't supported for HTML-style attributes.
-->

<!-- TODO
#### Boolean Attributes

Some attributes, such as "checked" for `input` tags or "selected" for `option` tags,
are "boolean" in the sense that their values don't matter -
it only matters whether or not they're present.
In HTML (but not XHTML), these attributes can be written as

    <input selected>

To do this in Scaml using hash-style attributes, just assign a Scala
`true` value to the attribute:

    %input{:selected => true}

In XHTML, the only valid value for these attributes is the name of the
attribute.  Thus this will render in XHTML as

    <input selected='selected'>

To set these attributes to false, simply assign them to a Scala false value.
In both XHTML and HTML

    %input{:selected => false}

will just render as

    <input>

HTML-style boolean attributes can be written just like HTML:

    %input(selected)

or using `true` and `false`:

    %input(selected=true)
-->

### Class and ID: `.` and `#`

The period and pound sign are borrowed from CSS.
They are used as shortcuts to specify the `class`
and `id` attributes of an element, respectively.
Multiple class names can be specified in a similar way to CSS,
by chaining the class names together with periods.
They are placed immediately after the tag and before an attributes hash.
For example:

    %div#things
      %span#rice Chicken Fried
      %p.beans{ :food => 'true' } The magical fruit
      %h1.class.otherclass#id La La La

is compiled to:

    <div id='things'>
      <span id='rice'>Chicken Fried</span>
      <p class='beans' food='true'>The magical fruit</p>
      <h1 id='id' class='class otherclass'>La La La</h1>
    </div>

And,

    #content
      .articles
        .article.title Doogie Howser Comes Out
        .article.date 2006-11-05
        .article.entry
          Neil Patrick Harris would like to dispel any rumors that he is straight

is compiled to:

    <div id='content'>
      <div class='articles'>
        <div class='article title'>Doogie Howser Comes Out</div>
        <div class='article date'>2006-11-05</div>
        <div class='article entry'>
          Neil Patrick Harris would like to dispel any rumors that he is straight
        </div>
      </div>
    </div>

#### Implicit Div Elements

Because divs are used so often, they're the default elements.
If you only define a class and/or id using `.` or `#`,
a div is automatically used.
For example:

    #collection
      .item
        .description What a cool item!

is the same as:

    %div#collection
      %div.item
        %div.description What a cool item!

and is compiled to:

    <div id='collection'>
      <div class='item'>
        <div class='description'>What a cool item!</div>
      </div>
    </div>

### Self-Closing Tags: `/`

The forward slash character, when placed at the end of a tag definition,
causes the tag to be self-closed.
For example:

    %br/
    %meta{'http-equiv' => 'Content-Type', :content => 'text/html'}/

is compiled to:

    <br />
    <meta http-equiv='Content-Type' content='text/html' />

<!-- TODO 
Some tags are automatically closed, as long as they have no content.
`meta`, `img`, `link`, `script`, `br`, and `hr` tags are closed by default.
This list can be customized by setting the [`:autoclose`](#autoclose-option) option.
For example:

    %br
    %meta{'http-equiv' => 'Content-Type', :content => 'text/html'}

is also compiled to:

    <br />
    <meta http-equiv='Content-Type' content='text/html' />
-->

### Whitespace Removal: `>` and `<`

`>` and `<` give you more control over the whitespace near a tag.
`>` will remove all whitespace surrounding a tag,
while `<` will remove all whitespace immediately within a tag.
You can think of them as alligators eating the whitespace:
`>` faces out of the tag and eats the whitespace on the outside,
and `<` faces into the tag and eats the whitespace on the inside.
They're placed at the end of a tag definition,
after class, id, and attribute declarations
but before `/` or `=`.
For example:

    %blockquote<
      %div
        Foo!

is compiled to:

    <blockquote><div>
      Foo!
    </div></blockquote>

And:

    %img
    %img>
    %img

is compiled to:

    <img /><img /><img />

And:

    %p<= "Foo\nBar"

is compiled to:

    <p>Foo
    Bar</p>

And finally:

    %img
    %pre><
      foo
      bar
    %img

is compiled to:

    <img /><pre>foo
    bar</pre><img />
    
<!-- TODO
### Object Reference: `[]`

Square brackets follow a tag definition and contain a Scala object
that is used to set the class and id of that tag.
The class is set to the object's class
(transformed to use underlines rather than camel case)
and the id is set to the object's class, followed by its id.
Because the id of an object is normally an obscure implementation detail,
this is most useful for elements that represent instances of Models.
Additionally, the second argument (if present) will be used as a prefix for
both the id and class attributes.
For example:

    # file: app/controllers/users_controller.rb

    def show
      user = CrazyUser.find(15)
    end

    -# file: app/views/users/show.haml

    %div[user, :greeting]
      %bar[290]/
      Hello!

is compiled to:

    <div class='greeting_crazy_user' id='greeting_crazy_user_15'>
      <bar class='fixnum' id='fixnum_581' />
      Hello!
    </div>

If you require that the class be something other than the underscored
object's class, you can implement the `haml_object_ref` method on the object.

    # file: app/models/crazy_user.rb

    class CrazyUser < ActiveRecord::Base
      def haml_object_ref
        "a_crazy_user"
      end
    end

    -# file: app/views/users/show.haml

    %div[user]
      Hello!

is compiled to:

    <div class='a_crazy_user' id='a_crazy_user_15'>
      Hello!
    </div>
-->
<!-- TODO
## Doctype: `!!!`

When describing HTML documents with Scaml,
you can have a document type or XML prolog generated automatically
by including the characters `!!!`.
For example:

    !!! XML
    !!!
    %html
      %head
        %title Myspace
      %body
        %h1 I am the international space station
        %p Sign my guestbook

is compiled to:

    <?xml version='1.0' encoding='utf-8' ?>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    <html>
      <head>
        <title>Myspace</title>
      </head>
      <body>
        <h1>I am the international space station</h1>
        <p>Sign my guestbook</p>
      </body>
    </html>

You can also specify the specific doctype after the `!!!`
When the [`:format`](#format) is set to `:xhtml` (the default),
the following doctypes are supported:

`!!!`
: XHTML 1.0 Transitional<br/>
 `<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">`

`!!! Strict`
: XHTML 1.0 Strict<br/>
 `<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">`

`!!! Frameset`
: XHTML 1.0 Frameset<br/>
 `<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">`

`!!! 5`
: XHTML 5<br/>
 `<!DOCTYPE html>`<br/>

`!!! 1.1`
: XHTML 1.1<br/>
 `<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">`

`!!! Basic`
: XHTML Basic 1.1<br/>
 `<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML Basic 1.1//EN" "http://www.w3.org/TR/xhtml-basic/xhtml-basic11.dtd"> `

`!!! Mobile`
: XHTML Mobile 1.2<br/>
 `<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.2//EN" "http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd">`

When the [`:format`](#format) option is set to `:html4`,
the following doctypes are supported:

`!!!`
: HTML 4.01 Transitional<br/>
 `<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">`

`!!! Strict`
: HTML 4.01 Strict<br/>
 `<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">`

`!!! Frameset`
: HTML 4.01 Frameset<br/>
 `<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">`

When the [`:format`](#format) option is set to `:html5`,
`!!!` is always `<!DOCTYPE html>`.

If you're not using the UTF-8 character set for your document,
you can specify which encoding should appear
in the XML prolog in a similar way.
For example:

    !!! XML iso-8859-1

is compiled to:

    <?xml version='1.0' encoding='iso-8859-1' ?>
-->

## Comments

Scaml supports two sorts of comments:
those that show up in the HTML output
and those that don't.

### HTML Comments: `/`

The forward slash character, when placed at the beginning of a line,
wraps all text after it in an HTML comment.
For example:

    %peanutbutterjelly
      / This is the peanutbutterjelly element
      I like sandwiches!

is compiled to:

    <peanutbutterjelly>
      <!-- This is the peanutbutterjelly element -->
      I like sandwiches!
    </peanutbutterjelly>

The forward slash can also wrap indented sections of code. For example:

    /
      %p This doesn't render...
      %div
        %h1 Because it's commented out!

is compiled to:

    <!--
      <p>This doesn't render...</p>
      <div>
        <h1>Because it's commented out!</h1>
      </div>
    -->

#### Conditional Comments: `/[]`

You can also use [Internet Explorer conditional comments](http://www.quirksmode.org/css/condcom.html)
by enclosing the condition in square brackets after the `/`.
For example:

    /[if IE]
      %a{ :href => 'http://www.mozilla.com/en-US/firefox/' }
        %h1 Get Firefox

is compiled to:

    <!--[if IE]>
      <a href='http://www.mozilla.com/en-US/firefox/'>
        <h1>Get Firefox</h1>
      </a>
    <![endif]-->

### Scaml Comments: `-#`

The hyphen followed immediately by the pound sign
signifies a silent comment.
Any text following this isn't rendered in the resulting document
at all.

For example:

    %p foo
    -# This is a comment
    %p bar

is compiled to:

    <p>foo</p>
    <p>bar</p>

You can also nest text beneath a silent comment.
None of this text will be rendered.
For example:

    %p foo
    -#
      This won't be displayed
        Nor will this
    %p bar

is compiled to:

    <p>foo</p>
    <p>bar</p>

## Scala Evaluation

### Binding Variables `-@`

When a Scalate template is rendered, the caller can pass an attribute map
which the template in charge of rendering. To bind the attribute to a Scala
variable, a Scaml template uses the hyphen character followed by a ampersand 
character and then a scala variable declaration statement.

For example To define an attribute use the following declaration

    -@ val foo: MyType 

If the attribute map does not contain a "foo" entry, then a 
NoValueSetException is thrown when the the template is rendered.

To avoid this exception, a default value can be configured.  For
example:

    -@ val bar: String = "this is the default value"

The attribute is now available for use as an expression. 

Its very common to have a template based on a single object who's members are f
frequently accessed.  In this cases, it's convenient to import all the object's 
members.  This can be done by adding the import keyword to the attribute declaration.

For example:

    -@ import val model: Person
    %p Hello #{name}, what is the weather like in your #{city}

is the same as:

    -@ val model: Person
    - import model._
    %p Hello #{name}, what is the weather like in your #{city}

Which is the same as:

    -@ val model: Person
    %p Hello #{model.name}, what is the weather like in your #{model.city}


### Inserting Scala: `=`

The equals character is followed by Scala code.
This code is evaluated and the output is inserted into the document.
For example:

    %p
      = List("hi", "there", "reader!").mkString(" ")
      = "yo"

is compiled to:

    <p>
      hi there reader!
      yo
    </p>

If the [`ScamlOptions.escape_html`](#escape_html-option) option is set, `=` will sanitize any
HTML-sensitive characters generated by the script. For example:

    = """<script>alert("I'm evil!");</script>"""

would be compiled to

    &lt;script&gt;alert(&quot;I'm evil!&quot;);&lt;/script&gt;

`=` can also be used at the end of a tag to insert Scala code within that tag.
For example:

    %p= "hello"

would be compiled to

    <p>hello</p>

### Running Scala: `-`

The hyphen character is also followed by Scala code.
This code is evaluated but *not* inserted into the document.

**It is not recommended that you use this widely;
almost all processing code and logic should be restricted
to the Controller, the Helper, or partials.**

For example:

    - var foo = "hello"
    - foo += " there"
    - foo += " you!"
    %p= foo

is compiled to:

    <p>hello there you!</p>

#### Scala Blocks

Scala blocks, like XHTML tags, don't need to be explicitly closed in Scaml.
Rather, they're automatically closed, based on indentation.
A block begins whenever the indentation is increased
after a Scala insertion or evaluation command.
It ends when the indentation decreases.

<!-- TODO
(as long as it's not an `else` clause or something similar).
-->

For example:

    - for(i <- 42 to 46)
      %p= i
    %p See, I can count!

is compiled to:

    <p>42</p>
    <p>43</p>
    <p>44</p>
    <p>45</p>
    <p>46</p>
    <p>See, I can count!</p>

Another example:

    %p
      - 2 match
        - case 1 =>
          = "one"
        - case 2 =>
          = "two"
        - case 3 =>
          = "three"

is compiled to:

    <p>
      two
    </p>
    
When inserting evaluated statements, it can also take advantage of Scala blocks. It can be handy
for passing partial functions.  For example:

    %p
      = List(1,2,3).foldLeft("result: ")
        - (a,x)=>
          - a+x 
          
Is the same as:

    %p
      = List(1,2,3).foldLeft("result: ") { (a,x)=> { a+x } }

would be compiled to

    <p>
      result: 123
    </p>
    
<!-- TODO
### Whitespace Preservation: `~` {#tilde}

`~` works just like `=`, except that it runs {Scaml::Helpers#find\_and\_preserve} on its input.
For example,

    ~ "Foo\n<pre>Bar\nBaz</pre>"

is the same as:

    = find_and_preserve("Foo\n<pre>Bar\nBaz</pre>")

and is compiled to:

    Foo
    <pre>Bar&#x000A;Baz</pre>

See also [Whitespace Preservation](#whitespace_preservation).
-->

### Scala Interpolation: `#{}`

Scala code can be interpolated within plain text using `#{}`.
For example:

    %p This is #{quality} cake!

is the same as

    %p= "This is the "+(quality)+" cake!"

and might compile to

    <p>This is scrumptious cake!</p>
    
<!-- TODO
Backslashes can be used to escape `#{` strings,
but they don't act as escapes anywhere else in the string.
For example:

    %p
      Look at \\#{h word} lack of backslash: \#{foo}
      And yon presence thereof: \{foo}

might compile to

    <p>
      Look at \yon lack of backslash: #{foo}
      And yon presence thereof: \{foo}
    </p>

Interpolation can also be used within [filters](#filters).
For example:

    :javascript
      $(document).ready(function() {
        alert(#{message.to_json});
      });

might compile to

    <script type='text/javascript'>
      //<![CDATA[
        $(document).ready(function() {
          alert("Hi there!");
        });
      //]]>
    </script>
-->

### Escaping HTML: `&=` {#escaping_html}

An ampersand followed by one or two equals characters
evaluates Scala code just like the equals without the ampersand,
but sanitizes any HTML-sensitive characters in the result of the code.
For example:

    &= "I like cheese & crackers"

compiles to

    I like cheese &amp; crackers

If the [`ScamlOptions.escape_html`](#escape_html-option) option is set,
`&=` behaves identically to `=`.

`&` can also be used on its own so that `#{}` interpolation is escaped.
For example,

    & I like #{"cheese & crackers"}

compiles to

    I like cheese &amp; crackers

### Unescaping HTML: `!=` {#unescaping_html}

An exclamation mark followed by one or two equals characters
evaluates Scala code just like the equals would,
but never sanitizes the HTML.

By default, the single equals doesn't sanitize HTML either.
However, if the [`:escape_html`](#escape_html-option) option is set,
`=` will sanitize the HTML, but `!=` still won't.
For example, if `:escape_html` is set:

    = "I feel <strong>!"
    != "I feel <strong>!"

compiles to

    I feel &lt;strong&gt;!
    I feel <strong>!

`!` can also be used on its own so that `#{}` interpolation is unescaped.
For example,

    ! I feel #{"<strong>"}!

compiles to

    I feel <strong>!
<!-- TODO
## Whitespace Preservation

Sometimes you don't want Scaml to indent all your text.
For example, tags like `pre` and `textarea` are whitespace-sensitive;
indenting the text makes them render wrong.

Scaml deals with this by "preserving" newlines before they're put into the document --
converting them to the XHTML whitespace escape code, `&#x000A;`.
Then Scaml won't try to re-format the indentation.

Literal `textarea` and `pre` tags automatically preserve content given through `=`.
Dynamically-generated `textarea`s and `pre`s can't be preserved automatically,
and so should be passed through {Scaml::Helpers#find\_and\_preserve} or the [`~` command](#tilde),
which has the same effect.

Blocks of literal text can be preserved using the [`:preserve` filter](#preserve-filter).
-->