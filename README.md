# quiescence
an ant task to turn off all messages for a section of the build

Apache 2 License

Quiescence allows you to turn off the output of a section of an ant build. This can come in handy when you want to run some very verbose commands, using the Recorder task, to pipe those results to a file. You may want to then parse that file to gather information, or determine success or failure. But the sub tasks produce so much noise that it is annoying to dump it also to the console. Quiescence allows you to do this. Used like


	<quiescence>
		<recorder name="/home/dave/dev/output.txt" action="start"/>
	
		<some_task_that_generates_lots_of_data/>
		<some_task_that_parses_lots_of_that_data/>
	
		<recorder name="/home/dave/dev/output.txt" action="stop"/>
	</quiescence>

Using this pattern, the output of the really noisy ant task will go into the file as specified, but won't make it to the console.
