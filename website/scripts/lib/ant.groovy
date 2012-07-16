//TODO wrap scp and ftp tasks

/**
 * Log into a remote host and run a given command.
 * 
 * e.g. 
 * sshexec(command: "ps", host: "filebot.sf.net", username: "rednoah", password: "correcthorsebatterystaple")
 */
def sshexec(param) {
	param << [trust: true] // auto-trust remote hosts
	ant().sshexec(param)
}


/**
 * Send email via smtp.
 * 
 * e.g. 
 * mail(mailhost:'smtp.gmail.com', mailport:'587', ssl:'no', enableStartTLS:'yes', user:'rednoah@gmail.com', password:'correcthorsebatterystaple', from:'rednoah@gmail.com', to:'someone@gmail.com', subject:'Hello Ant World', message:'Dear Ant, ...')
 */
def mail(param) {
	def sender    = param.remove('from')
	def recipient = param.remove('to')
	
	ant().mail(param) {
		from(address:sender)
		to(address:recipient)
	}
}


/**
 * Send email using gmail default settings.
 *
 * e.g.
 * gmail(subject:'Hello Ant World', message:'Dear Ant, ...', to:'someone@gmail.com', user:'rednoah', password:'correcthorsebatterystaple')
 */
def gmail(param) {
	param << [mailhost:'smtp.gmail.com', mailport:'587', ssl:'no', enableStartTLS:'yes']
	param << [user:param.username ? param.remove('username') + "@gmail.com" : param.user]
	param << [from: param.from ?: param.user]
	
	mail(param)
}


def ant() {
	return new AntBuilder()
}